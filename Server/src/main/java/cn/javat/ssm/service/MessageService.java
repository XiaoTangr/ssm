package cn.javat.ssm.service;

import cn.javat.ssm.entity.Message;
import java.util.List;

/**
 * 留言服务接口
 */
public interface MessageService {
    /**
     * 发布留言
     * @param message 留言实体
     * @return 是否发布成功
     */
    boolean postMessage(Message message);
    
    /**
     * 编辑留言
     * @param message 留言实体
     * @return 是否编辑成功
     */
    boolean editMessage(Message message);
    
    /**
     * 删除留言
     * @param messageId 留言ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteMessage(Long messageId, Long userId);
    
    /**
     * 根据ID获取留言详情
     * @param id 留言ID
     * @return 留言实体
     */
    Message getMessageById(Long id);
    
    /**
     * 分页获取留言列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 留言列表
     */
    List<Message> getMessageList(int pageNum, int pageSize);
    
    /**
     * 搜索留言
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 留言列表
     */
    List<Message> searchMessages(String keyword, int pageNum, int pageSize);
}