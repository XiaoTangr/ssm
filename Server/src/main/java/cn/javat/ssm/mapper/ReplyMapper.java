package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.Reply;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 回复Mapper接口
 */
public interface ReplyMapper {
    /**
     * 根据ID查询回复
     * @param id 回复ID
     * @return 回复实体
     */
    Reply selectById(@Param("id") Long id);
    
    /**
     * 根据留言ID查询回复列表
     * @param messageId 留言ID
     * @return 回复列表
     */
    List<Reply> selectByMessageId(@Param("messageId") Long messageId);
    
    /**
     * 插入回复
     * @param reply 回复实体
     * @return 影响行数
     */
    int insert(Reply reply);
    
    /**
     * 根据ID删除回复（逻辑删除）
     * @param id 回复ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}