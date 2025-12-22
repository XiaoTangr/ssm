package cn.javat.ssm.service.impl;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.Reply;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.mapper.ReplyMapper;
import cn.javat.ssm.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyMapper replyMapper;

    @Autowired
    public ReplyServiceImpl(ReplyMapper replyMapper) {
        this.replyMapper = replyMapper;
    }

    @Override
    public ServiceResult<Map<String, Object>> getRepliesByMessageId(Long messageId, int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        // 查询回复列表
        List<Reply> list = replyMapper.selectList(messageId, offset, pageSize);
        
        // 统计总数
        int total = replyMapper.count(messageId);
        
        // 构造返回结果
        result.put("total", total);
        result.put("list", list);
        
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult<Long> createReply(Long messageId, Long parentId, String content, User user) {
        Reply reply = new Reply();
        reply.setMessageId(messageId);
        reply.setCreatorId(user.getId());
        reply.setParentId(parentId);
        reply.setContent(content);
        reply.setCreateTime(System.currentTimeMillis());
        reply.setIsDeleted(0);
        
        if (replyMapper.insert(reply) > 0) {
            return ServiceResult.ok(reply.getId());
        }
        return ServiceResult.error(-1);
    }

    @Override
    public ServiceResult<Boolean> deleteReply(Long replyId, User user) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            return ServiceResult.error(404);
        }
        
        // 检查权限（创建者或管理员）
        if (!reply.getCreatorId().equals(user.getId()) && user.getRole() != 1) {
            return ServiceResult.error(403);
        }
        
        if (replyMapper.deleteById(replyId) > 0) {
            return ServiceResult.ok(true);
        }
        return ServiceResult.error(-1);
    }
}