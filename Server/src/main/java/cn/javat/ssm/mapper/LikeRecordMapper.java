package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.LikeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞记录Mapper接口
 */
@Mapper
public interface LikeRecordMapper {
    /**
     * 根据ID查询点赞记录
     *
     * @param id 点赞记录ID
     * @return 点赞记录实体
     */
    LikeRecord selectById(@Param("id") Long id);

    /**
     * 根据用户ID和留言ID查询点赞记录
     *
     * @param userId 用户ID
     * @param messageId 留言ID
     * @return 点赞记录实体
     */
    LikeRecord selectByUserIdAndMessageId(@Param("userId") Long userId, @Param("messageId") Long messageId);

    /**
     * 根据用户ID和留言ID统计点赞记录数量
     *
     * @param userId 用户ID
     * @param messageId 留言ID
     * @return 点赞记录数量
     */
    int countByUserIdAndMessageId(@Param("userId") Long userId, @Param("messageId") Long messageId);

    /**
     * 根据用户ID查询点赞记录列表
     *
     * @param userId 用户ID
     * @return 点赞记录列表
     */
    List<LikeRecord> selectListByUserId(@Param("userId") Long userId);

    /**
     * 根据留言ID查询点赞记录列表
     *
     * @param messageId 留言ID
     * @return 点赞记录列表
     */
    List<LikeRecord> selectListByMessageId(@Param("messageId") Long messageId);

    /**
     * 插入点赞记录
     *
     * @param likeRecord 点赞记录实体
     * @return 影响行数
     */
    int insert(LikeRecord likeRecord);

    /**
     * 更新点赞记录
     *
     * @param likeRecord 点赞记录实体
     * @return 影响行数
     */
    int update(LikeRecord likeRecord);

    /**
     * 根据ID删除点赞记录
     *
     * @param id 点赞记录ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}