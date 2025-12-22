package cn.javat.ssm.service;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import java.util.Map;

/**
 * 点赞服务接口
 */
public interface LikeRecordService {
    /**
     * 点赞/取消点赞留言
     *
     * @param messageId 留言ID
     * @param isCancel 是否取消
     * @param user 用户
     * @return 最新点赞数
     */
    ServiceResult<Integer> likeMessage(Long messageId, Boolean isCancel, User user);

    /**
     * 查询用户点赞状态
     *
     * @param messageId 留言ID
     * @param user 用户
     * @return 是否已点赞
     */
    ServiceResult<Boolean> getLikeStatus(Long messageId, User user);
}