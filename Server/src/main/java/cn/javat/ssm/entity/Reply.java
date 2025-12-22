package cn.javat.ssm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回复实体类
 * 对应数据库中的reply表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    /**
     * 回复唯一ID
     */
    private Long id;

    /**
     * 所属主留言ID（关联message.id）
     */
    private Long messageId;

    /**
     * 回复者ID（关联user.id）
     */
    private Long creatorId;

    /**
     * 回复父ID：NULL-回复主留言 非NULL-多级回复
     */
    private Long parentId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 创建时间戳（毫秒）
     */
    private Long createTime;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer isDeleted;
}