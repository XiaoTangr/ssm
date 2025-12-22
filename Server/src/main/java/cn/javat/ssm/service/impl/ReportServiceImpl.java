package cn.javat.ssm.service.impl;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.Report;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.mapper.MessageMapper;
import cn.javat.ssm.mapper.ReportMapper;
import cn.javat.ssm.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public ReportServiceImpl(ReportMapper reportMapper, MessageMapper messageMapper) {
        this.reportMapper = reportMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public ServiceResult<Long> reportMessage(Long messageId, String reason, User user) {
        // 检查留言是否存在
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return ServiceResult.error(404);
        }
        
        // 创建举报记录
        Report report = new Report();
        report.setUserId(user.getId());
        report.setMessageId(messageId);
        report.setReason(reason);
        report.setCreateTime(System.currentTimeMillis());
        report.setAuditStatus(0); // 待审核
        report.setAuditUserId(null);
        report.setAuditTime(null);
        
        if (reportMapper.insert(report) > 0) {
            return ServiceResult.ok(report.getId());
        }
        return ServiceResult.error(-1);
    }

    @Override
    public ServiceResult<Map<String, Object>> getReports(int pageNum, int pageSize, Integer auditStatus, User user) {
        // 检查是否是管理员
        if (user.getRole() != 1) {
            return ServiceResult.error(403);
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        // 查询举报列表
        List<Report> list = reportMapper.selectList(auditStatus, offset, pageSize);
        
        // 统计总数
        int total = reportMapper.count(auditStatus);
        
        // 构造返回结果
        result.put("total", total);
        result.put("list", list);
        
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult<Boolean> auditReport(Long reportId, Integer auditStatus, String remark, User user) {
        // 检查是否是管理员
        if (user.getRole() != 1) {
            return ServiceResult.error(403);
        }
        
        // 查询举报记录
        Report report = reportMapper.selectById(reportId);
        if (report == null) {
            return ServiceResult.error(404);
        }
        
        // 更新举报记录
        report.setAuditStatus(auditStatus);
        report.setAuditUserId(user.getId());
        report.setAuditTime(System.currentTimeMillis());
        
        if (reportMapper.update(report) > 0) {
            // 如果审核通过，更新留言状态为违规
            if (auditStatus == 1) { // 审核通过
                Message message = messageMapper.selectById(report.getMessageId());
                if (message != null) {
                    messageMapper.updateStatus(message.getId(), -2); // 违规状态
                }
            }
            return ServiceResult.ok(true);
        }
        return ServiceResult.error(-1);
    }
}