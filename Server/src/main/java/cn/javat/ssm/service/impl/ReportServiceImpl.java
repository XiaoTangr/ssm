package cn.javat.ssm.service.impl;

import cn.javat.ssm.mapper.ReportMapper;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.mapper.UserMapper;
import cn.javat.ssm.entity.Report;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportMapper reportMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public boolean reportMessage(Report report) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(report.getMessageId());
        if (message == null) {
            return false;
        }
        
        // 检查用户是否存在
        User user = userMapper.selectById(report.getUserId());
        if (user == null) {
            return false;
        }
        
        int result = reportMapper.insert(report);
        return result > 0;
    }
    
    @Override
    public boolean auditReport(Long reportId, int auditStatus, Long adminUserId) {
        // 检查管理员是否存在且具有管理员权限
        User adminUser = userMapper.selectById(adminUserId);
        if (adminUser == null || adminUser.getRole() != 1) {
            return false;
        }
        
        // 更新举报记录的审核状态
        int result = reportMapper.updateAuditStatus(
            reportId, 
            auditStatus, 
            adminUserId, 
            System.currentTimeMillis()
        );
        
        // 如果审核通过，更新留言状态为违规
        if (auditStatus == 1) {
            Report report = reportMapper.selectById(reportId);
            if (report != null) {
                Message message = new Message();
                message.setId(report.getMessageId());
                message.setStatus(-2); // 违规状态
                messageMapper.update(message);
            }
        }
        
        return result > 0;
    }
}