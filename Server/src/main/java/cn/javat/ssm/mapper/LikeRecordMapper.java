package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.LikeRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞记录Mapper接口
 */
public interface LikeRecordMapper {
    /**
     * 根据用户ID和留言ID查询点赞记录
     * @param userId 用户ID
     * @param messageId 留言ID
     * @return 点赞记录
     */
    LikeRecord selectByUserAndMessage(@Param("userId") Long userId, @Param("messageId") Long messageId);
    
    /**
     * 插入点赞记录
     * @param likeRecord 点赞记录实体
     * @return 影响行数
     */
    int insert(LikeRecord likeRecord);
    
    /**
     * 更新点赞记录状态
     * @param id 记录ID
     * @param isCanceled 取消状态
     * @return 影响行数
     */
    int updateCanceled(@Param("id") Long id, @Param("isCanceled") Integer isCanceled);
}