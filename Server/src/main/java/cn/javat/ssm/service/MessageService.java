package cn.javat.ssm.service;

import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 留言服务接口
 */
public interface MessageService {
    /**
     * 分页查询留言列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param sortType 排序类型
     * @param keyword 搜索关键词
     * @param user 当前用户
     * @return 留言列表
     */
    ServiceResult<Map<String, Object>> getMessageList(int pageNum, int pageSize, String sortType, String keyword, User user);

    /**
     * 获取单条留言详情
     *
     * @param messageId 留言ID
     * @param user 当前用户
     * @return 留言详情
     */
    ServiceResult<Map<String, Object>> getMessageDetail(Long messageId, User user);

    /**
     * 发表留言
     *
     * @param title 标题
     * @param content 内容
     * @param user 用户
     * @return 留言ID
     */
    ServiceResult<Long> createMessage(String title, String content, User user);

    /**
     * 编辑留言
     *
     * @param messageId 留言ID
     * @param title 标题
     * @param content 内容
     * @param user 用户
     * @return 更新时间
     */
    ServiceResult<Long> updateMessage(Long messageId, String title, String content, User user);

    /**
     * 删除留言
     *
     * @param messageId 留言ID
     * @param user 用户
     * @return 删除结果
     */
    ServiceResult<Boolean> deleteMessage(Long messageId, User user);

    /**
     * 更新留言状态
     *
     * @param messageId 留言ID
     * @param status 状态
     * @param user 用户
     * @return 更新结果
     */
    ServiceResult<Boolean> updateMessageStatus(Long messageId, Integer status, User user);
}