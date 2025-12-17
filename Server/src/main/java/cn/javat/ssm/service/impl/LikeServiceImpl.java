package cn.javat.ssm.service.impl;

import cn.javat.ssm.mapper.LikeRecordMapper;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.entity.LikeRecord;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("likeService")
public class LikeServiceImpl implements LikeService {
    
    @Autowired
    private LikeRecordMapper likeRecordMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Override
    public int likeMessage(Long messageId, Long userId, boolean isCancel) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return -1; // 留言不存在
        }
        
        // 查找点赞记录
        LikeRecord likeRecord = likeRecordMapper.selectByUserAndMessage(userId, messageId);
        
        if (isCancel) {
            // 取消点赞
            if (likeRecord != null && likeRecord.getIsCanceled() == 0) {
                likeRecordMapper.updateCanceled(likeRecord.getId(), 1);
                messageMapper.updateLikeCount(messageId, -1);
                return message.getLikeCount() - 1;
            }
        } else {
            // 点赞
            if (likeRecord == null) {
                // 创建新的点赞记录
                LikeRecord newLikeRecord = new LikeRecord();
                newLikeRecord.setUserId(userId);
                newLikeRecord.setMessageId(messageId);
                newLikeRecord.setCreateTime(System.currentTimeMillis());
                newLikeRecord.setIsCanceled(0);
                likeRecordMapper.insert(newLikeRecord);
                
                messageMapper.updateLikeCount(messageId, 1);
                return message.getLikeCount() + 1;
            } else if (likeRecord.getIsCanceled() == 1) {
                // 恢复之前的点赞
                likeRecordMapper.updateCanceled(likeRecord.getId(), 0);
                messageMapper.updateLikeCount(messageId, 1);
                return message.getLikeCount() + 1;
            }
        }
        
        return message.getLikeCount();
    }
    
    @Override
    public boolean isLiked(Long messageId, Long userId) {
        LikeRecord likeRecord = likeRecordMapper.selectByUserAndMessage(userId, messageId);
        return likeRecord != null && likeRecord.getIsCanceled() == 0;
    }
}