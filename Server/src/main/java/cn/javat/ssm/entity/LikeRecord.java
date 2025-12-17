package cn.javat.ssm.entity;

/**
 * 点赞记录实体类
 * 对应数据库中的like_record表
 */
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(Integer isCanceled) {
        this.isCanceled = isCanceled;
    }
}