package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留言Mapper接口
 */
@Mapper
public interface MessageMapper {
    /**
     * 根据ID查询留言
     *
     * @param id 留言ID
     * @return 留言实体
     */
    Message selectById(@Param("id") Long id);

    /**
     * 分页查询留言列表
     *
     * @param offset 偏移量
     * @param limit  每页数量
     * @param keyword 关键词
     * @return 留言列表
     */
    List<Message> selectList(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);

    /**
     * 根据状态和创建时间倒序查询留言列表
     *
     * @param status 状态
     * @param offset 偏移量
     * @param limit  每页数量
     * @return 留言列表
     */
    List<Message> selectListByStatusOrderByCreateTimeDesc(@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据状态和点赞数、创建时间倒序查询留言列表
     *
     * @param status 状态
     * @param offset 偏移量
     * @param limit  每页数量
     * @return 留言列表
     */
    List<Message> selectListByStatusOrderByLikeCountAndCreateTimeDesc(@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据关键词按创建时间倒序查询留言列表
     *
     * @param keyword 关键词
     * @param offset 偏移量
     * @param limit  每页数量
     * @return 留言列表
     */
    List<Message> selectListByKeywordOrderByCreateTimeDesc(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据关键词按点赞数和创建时间倒序查询留言列表
     *
     * @param keyword 关键词
     * @param offset 偏移量
     * @param limit  每页数量
     * @return 留言列表
     */
    List<Message> selectListByKeywordOrderByLikeCountAndCreateTimeDesc(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据创建者ID查询留言列表
     *
     * @param creatorId 创建者ID
     * @return 留言列表
     */
    List<Message> selectListByCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 统计留言总数
     *
     * @return 总数
     */
    int count();

    /**
     * 根据状态统计留言总数
     *
     * @param status 状态
     * @return 总数
     */
    int countByStatus(@Param("status") Integer status);

    /**
     * 根据关键词统计留言总数
     *
     * @param keyword 关键词
     * @return 总数
     */
    int countByKeyWord(@Param("keyword") String keyword);

    /**
     * 根据状态和关键词统计留言总数
     *
     * @param status 状态
     * @param keyword 关键词
     * @return 总数
     */
    int countByStatusAndKeyWord(@Param("status") Integer status, @Param("keyword") String keyword);

    /**
     * 插入留言
     *
     * @param message 留言实体
     * @return 影响行数
     */
    int insert(Message message);

    /**
     * 更新留言
     *
     * @param message 留言实体
     * @return 影响行数
     */
    int update(Message message);

    /**
     * 更新留言状态
     *
     * @param id 留言ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据ID删除留言（逻辑删除）
     *
     * @param id 留言ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}