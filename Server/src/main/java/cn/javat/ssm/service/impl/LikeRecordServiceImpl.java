package cn.javat.ssm.service.impl;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.LikeRecord;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.mapper.LikeRecordMapper;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.service.LikeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeRecordServiceImpl implements LikeRecordService {

    private final LikeRecordMapper likeRecordMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public LikeRecordServiceImpl(LikeRecordMapper likeRecordMapper, MessageMapper messageMapper) {
        this.likeRecordMapper = likeRecordMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public ServiceResult<Integer> likeMessage(Long messageId, Boolean isCancel, User user) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }
        
        // 检查是否已有点赞记录
        LikeRecord likeRecord = likeRecordMapper.selectByUserIdAndMessageId(user.getId(), messageId);
        
        // 如果是取消点赞
        if (Boolean.TRUE.equals(isCancel)) {
            if (likeRecord != null && likeRecord.getIsCanceled() == 0) {
                // 更新点赞记录为已取消
                likeRecord.setIsCanceled(1);
                likeRecordMapper.update(likeRecord);
                
                // 减少留言的点赞数
                message.setLikeCount(Math.max(0, message.getLikeCount() - 1));
                messageMapper.update(message);
                
                return ServiceResult.ok(message.getLikeCount());
            }
            return ServiceResult.error(-1); // 未点赞或已取消
        }
        
        // 如果是点赞
        if (likeRecord == null) {
            // 创建新的点赞记录
            likeRecord = new LikeRecord();
            likeRecord.setUserId(user.getId());
            likeRecord.setMessageId(messageId);
            likeRecord.setCreateTime(System.currentTimeMillis());
            likeRecord.setIsCanceled(0);
            likeRecordMapper.insert(likeRecord);
            
            // 增加留言的点赞数
            message.setLikeCount(message.getLikeCount() + 1);
            messageMapper.update(message);
        } else if (likeRecord.getIsCanceled() == 1) {
            // 如果之前已取消，则恢复
            likeRecord.setIsCanceled(0);
            likeRecord.setCreateTime(System.currentTimeMillis());
            likeRecordMapper.update(likeRecord);
            
            // 增加留言的点赞数
            message.setLikeCount(message.getLikeCount() + 1);
            messageMapper.update(message);
        }
        
        return ServiceResult.ok(message.getLikeCount());
    }

    @Override
    public ServiceResult<Boolean> getLikeStatus(Long messageId, User user) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }
        
        // 检查是否已有点赞记录
        LikeRecord likeRecord = likeRecordMapper.selectByUserIdAndMessageId(user.getId(), messageId);
        
        // 判断是否已点赞且未取消
        boolean isLiked = (likeRecord != null && likeRecord.getIsCanceled() == 0);
        
        return ServiceResult.ok(isLiked);
    }
}