package cn.javat.ssm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞记录实体类
 * 对应数据库中的like_record表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRecord {
    /**
     * 点赞记录ID
     */
    private Long id;

    /**
     * 点赞用户ID（关联user.id）
     */
    private Long userId;

    /**
     * 点赞的主留言ID（关联message.id）
     */
    private Long messageId;

    /**
     * 点赞时间戳（毫秒）
     */
    private Long createTime;

    /**
     * 取消状态：0-未取消 1-已取消
     */
    private Integer isCanceled;
}