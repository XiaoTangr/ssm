package cn.javat.ssm.service;

/**
 * 点赞服务接口
 */
public interface LikeService {
    /**
     * 点赞/取消点赞留言
     * @param messageId 留言ID
     * @param userId 用户ID
     * @param isCancel 是否取消点赞
     * @return 最新点赞数
     */
    int likeMessage(Long messageId, Long userId, boolean isCancel);
    
    /**
     * 查询用户是否已点赞指定留言
     * @param messageId 留言ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean isLiked(Long messageId, Long userId);
}