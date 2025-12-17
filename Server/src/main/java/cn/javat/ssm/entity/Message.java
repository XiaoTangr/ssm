package cn.javat.ssm.entity;

/**
 * 留言实体类
 * 对应数据库中的message表
 */
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}