package cn.javat.ssm.service;

import cn.javat.ssm.entity.Reply;
import java.util.List;

/**
 * 回复服务接口
 */
public interface ReplyService {
    /**
     * 发布回复
     * @param reply 回复实体
     * @return 是否发布成功
     */
    boolean postReply(Reply reply);
    
    /**
     * 删除回复
     * @param replyId 回复ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteReply(Long replyId, Long userId);
    
    /**
     * 根据留言ID获取回复列表
     * @param messageId 留言ID
     * @return 回复列表
     */
    List<Reply> getRepliesByMessageId(Long messageId);
}