package cn.javat.ssm.service.impl;

import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.mapper.UserMapper;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public boolean postMessage(Message message) {
        int result = messageMapper.insert(message);
        return result > 0;
    }
    
    @Override
    public boolean editMessage(Message message) {
        Message existingMessage = messageMapper.selectById(message.getId());
        if (existingMessage == null) {
            return false;
        }
        
        int result = messageMapper.update(message);
        return result > 0;
    }
    
    @Override
    public boolean deleteMessage(Long messageId, Long userId) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return false;
        }
        
        // 检查是否有权限删除（创建者或管理员）
        User user = userMapper.selectById(userId);
        if (!message.getCreatorId().equals(userId) && user.getRole() != 1) {
            return false;
        }
        
        int result = messageMapper.deleteById(messageId);
        return result > 0;
    }
    
    @Override
    public Message getMessageById(Long id) {
        return messageMapper.selectById(id);
    }
    
    @Override
    public List<Message> getMessageList(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return messageMapper.selectList(offset, pageSize);
    }
    
    @Override
    public List<Message> searchMessages(String keyword, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return messageMapper.selectByKeyword(keyword, offset, pageSize);
    }
}