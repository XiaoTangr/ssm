package cn.javat.ssm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 留言实体类
 * 对应数据库中的message表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * 留言唯一ID
     */
    private Long id;

    /**
     * 留言标题
     */
    private String title;

    /**
     * 留言内容（支持富文本）
     */
    private String content;

    /**
     * 获赞数
     */
    private Integer likeCount;

    /**
     * 创建者ID（关联user.id）
     */
    private Long creatorId;

    /**
     * 创建时间戳（毫秒）
     */
    private Long createTime;

    /**
     * 更新时间戳（毫秒）
     */
    private Long updateTime;

    /**
     * 状态：-2-违规 -1-待审核/停用 0-正常
     */
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer isDeleted;
}