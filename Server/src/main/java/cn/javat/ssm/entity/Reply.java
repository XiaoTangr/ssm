package cn.javat.ssm.entity;

/**
 * 回复实体类
 * 对应数据库中的reply表
 */
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}