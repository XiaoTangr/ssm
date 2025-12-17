package cn.javat.ssm.service.impl;

import cn.javat.ssm.mapper.ReplyMapper;
import cn.javat.ssm.mapper.UserMapper;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.entity.Reply;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    
    @Autowired
    private ReplyMapper replyMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Override
    public boolean postReply(Reply reply) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(reply.getMessageId());
        if (message == null) {
            return false;
        }
        
        int result = replyMapper.insert(reply);
        return result > 0;
    }
    
    @Override
    public boolean deleteReply(Long replyId, Long userId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            return false;
        }
        
        // 检查是否有权限删除（创建者或管理员）
        User user = userMapper.selectById(userId);
        if (!reply.getCreatorId().equals(userId) && user.getRole() != 1) {
            return false;
        }
        
        int result = replyMapper.deleteById(replyId);
        return result > 0;
    }
    
    @Override
    public List<Reply> getRepliesByMessageId(Long messageId) {
        return replyMapper.selectByMessageId(messageId);
    }
}