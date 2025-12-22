package cn.javat.ssm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 举报记录实体类
 * 对应数据库中的report表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}