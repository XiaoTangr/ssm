package cn.javat.ssm.service;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.Reply;
import cn.javat.ssm.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 回复服务接口
 */
public interface ReplyService {
    /**
     * 分页查询留言回复
     *
     * @param messageId 留言ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 回复列表
     */
    ServiceResult<Map<String, Object>> getRepliesByMessageId(Long messageId, int pageNum, int pageSize);

    /**
     * 发表回复
     *
     * @param messageId 所属留言ID
     * @param parentId 父回复ID
     * @param content 回复内容
     * @param user 用户
     * @return 回复ID
     */
    ServiceResult<Long> createReply(Long messageId, Long parentId, String content, User user);

    /**
     * 删除回复
     *
     * @param replyId 回复ID
     * @param user 用户
     * @return 是否删除成功
     */
    ServiceResult<Boolean> deleteReply(Long replyId, User user);
}