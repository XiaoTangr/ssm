package cn.javat.ssm.service;

import cn.javat.ssm.entity.Report;

/**
 * 举报服务接口
 */
public interface ReportService {
    /**
     * 举报留言
     * @param report 举报实体
     * @return 是否举报成功
     */
    boolean reportMessage(Report report);
    
    /**
     * 管理员审核举报
     * @param reportId 举报ID
     * @param auditStatus 审核状态
     * @param adminUserId 管理员ID
     * @return 是否审核成功
     */
    boolean auditReport(Long reportId, int auditStatus, Long adminUserId);
}