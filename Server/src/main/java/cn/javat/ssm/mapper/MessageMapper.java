package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.Message;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 留言Mapper接口
 */
public interface MessageMapper {
    /**
     * 根据ID查询留言
     * @param id 留言ID
     * @return 留言实体
     */
    Message selectById(@Param("id") Long id);
    
    /**
     * 分页查询留言列表
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 留言列表
     */
    List<Message> selectList(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据关键字搜索留言
     * @param keyword 关键字
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 留言列表
     */
    List<Message> selectByKeyword(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 插入留言
     * @param message 留言实体
     * @return 影响行数
     */
    int insert(Message message);
    
    /**
     * 更新留言
     * @param message 留言实体
     * @return 影响行数
     */
    int update(Message message);
    
    /**
     * 根据ID删除留言（逻辑删除）
     * @param id 留言ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新留言点赞数
     * @param id 留言ID
     * @param count 点赞数变化值
     * @return 影响行数
     */
    int updateLikeCount(@Param("id") Long id, @Param("count") int count);
}