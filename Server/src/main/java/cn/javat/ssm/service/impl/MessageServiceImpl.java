package cn.javat.ssm.service.impl;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.mapper.LikeRecordMapper;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.mapper.ReplyMapper;
import cn.javat.ssm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final ReplyMapper replyMapper;
    private final LikeRecordMapper likeRecordMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, ReplyMapper replyMapper, LikeRecordMapper likeRecordMapper) {
        this.messageMapper = messageMapper;
        this.replyMapper = replyMapper;
        this.likeRecordMapper = likeRecordMapper;
    }

    @Override
    public ServiceResult<Map<String, Object>> getMessageList(int pageNum, int pageSize, String sortType, String keyword, User user) {
        Map<String, Object> result = new HashMap<>();

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        List<Message> list;
        int total;

        // 判断用户是否为管理员（role=1为管理员）
        boolean isAdmin = user != null && user.getRole() == 1;
        
        // 根据排序类型、关键词和用户角色查询
        if ("time".equals(sortType)) {
            // 按时间排序
            if (keyword != null && !keyword.isEmpty()) {
                list = messageMapper.selectListByKeywordOrderByCreateTimeDesc(keyword, offset, pageSize);
                if (isAdmin) {
                    // 管理员不过滤status
                    total = messageMapper.countByKeyWord(keyword);
                } else {
                    // 普通用户只统计status=0的留言
                    total = messageMapper.countByStatusAndKeyWord(0, keyword);
                }
            } else {
                if (isAdmin) {
                    // 管理员查询所有状态的留言
                    list = messageMapper.selectList(offset, pageSize, null);
                    total = messageMapper.count();
                } else {
                    // 普通用户只查询status=0的留言
                    list = messageMapper.selectListByStatusOrderByCreateTimeDesc(0, offset, pageSize);
                    total = messageMapper.countByStatus(0);
                }
            }
        } else {
            // 默认按热度排序
            if (keyword != null && !keyword.isEmpty()) {
                list = messageMapper.selectListByKeywordOrderByLikeCountAndCreateTimeDesc(keyword, offset, pageSize);
                if (isAdmin) {
                    // 管理员不过滤status
                    total = messageMapper.countByKeyWord(keyword);
                } else {
                    // 普通用户只统计status=0的留言
                    total = messageMapper.countByStatusAndKeyWord(0, keyword);
                }
            } else {
                if (isAdmin) {
                    // 管理员查询所有状态的留言
                    list = messageMapper.selectList(offset, pageSize, null);
                    total = messageMapper.count();
                } else {
                    // 普通用户只查询status=0的留言
                    list = messageMapper.selectListByStatusOrderByLikeCountAndCreateTimeDesc(0, offset, pageSize);
                    total = messageMapper.countByStatus(0);
                }
            }
        }

        // 构造返回结果
        result.put("total", total);
        result.put("list", list);

        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult<Map<String, Object>> getMessageDetail(Long messageId, User user) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", message.getId());
        result.put("title", message.getTitle());
        result.put("content", message.getContent());
        result.put("likeCount", message.getLikeCount());
        result.put("creatorId", message.getCreatorId());
        result.put("createTime", message.getCreateTime());
        result.put("updateTime", message.getUpdateTime());
        result.put("status", message.getStatus());

        // 判断当前用户是否点赞
        boolean isLiked = user != null && likeRecordMapper.countByUserIdAndMessageId(user.getId(), messageId) > 0;
        result.put("isLiked", isLiked);

        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult<Long> createMessage(String title, String content, User user) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setLikeCount(0);
        message.setCreatorId(user.getId());
        long currentTime = System.currentTimeMillis();
        message.setCreateTime(currentTime);
        message.setUpdateTime(currentTime);
        message.setStatus(0);
        message.setIsDeleted(0);

        if (messageMapper.insert(message) > 0) {
            return ServiceResult.ok(message.getId());
        }
        return ServiceResult.error(-1);
    }

    @Override
    public ServiceResult<Long> updateMessage(Long messageId, String title, String content, User user) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }

        // 检查是否是创建者
        if (!message.getCreatorId().equals(user.getId())) {
            return ServiceResult.error(403);
        }

        message.setTitle(title);
        message.setContent(content);
        message.setUpdateTime(System.currentTimeMillis());

        if (messageMapper.update(message) > 0) {
            return ServiceResult.ok(message.getUpdateTime());
        }
        return ServiceResult.error(-1);
    }

    @Override
    public ServiceResult<Boolean> deleteMessage(Long messageId, User user) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }

        // 检查权限（创建者或管理员）
        if (!message.getCreatorId().equals(user.getId()) && user.getRole() != 1) {
            return ServiceResult.error(403);
        }

        if (messageMapper.deleteById(messageId) > 0) {
            return ServiceResult.ok(true);
        }
        return ServiceResult.error(-1);
    }

    @Override
    public ServiceResult<Boolean> updateMessageStatus(Long messageId, Integer status, User user) {
        // 检查是否是管理员
        if (user.getRole() != 1) {
            return ServiceResult.error(403);
        }

        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }

        if (messageMapper.updateStatus(messageId, status) > 0) {
            return ServiceResult.ok(true);
        }
        return ServiceResult.error(-1);
    }
}