package cn.javat.ssm.service;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import java.util.Map;

/**
 * 举报服务接口
 */
public interface ReportService {
    /**
     * 举报留言
     *
     * @param messageId 被举报留言ID
     * @param reason 举报原因
     * @param user 用户
     * @return 举报ID
     */
    ServiceResult<Long> reportMessage(Long messageId, String reason, User user);

    /**
     * 管理员查询举报列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param auditStatus 审核状态
     * @param user 用户
     * @return 举报列表
     */
    ServiceResult<Map<String, Object>> getReports(int pageNum, int pageSize, Integer auditStatus, User user);

    /**
     * 管理员审核举报
     *
     * @param reportId 举报ID
     * @param auditStatus 审核状态
     * @param remark 审核备注
     * @param user 用户
     * @return 是否审核成功
     */
    ServiceResult<Boolean> auditReport(Long reportId, Integer auditStatus, String remark, User user);
}