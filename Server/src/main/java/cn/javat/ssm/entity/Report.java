package cn.javat.ssm.entity;

/**
 * 举报记录实体类
 * 对应数据库中的report表
 */
public class Report {
    /**
     * 举报记录ID
     */
    private Long id;
    
    /**
     * 举报用户ID（关联user.id）
     */
    private Long userId;
    
    /**
     * 举报的主留言ID（关联message.id）
     */
    private Long messageId;
    
    /**
     * 举报原因
     */
    private String reason;
    
    /**
     * 举报时间戳（毫秒）
     */
    private Long createTime;
    
    /**
     * 审核状态：0-待审核 1-审核通过 2-审核驳回
     */
    private Integer auditStatus;
    
    /**
     * 审核管理员ID（关联user.id）
     */
    private Long auditUserId;
    
    /**
     * 审核时间戳（毫秒）
     */
    private Long auditTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }
}