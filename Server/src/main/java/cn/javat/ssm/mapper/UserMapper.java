package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据邮箱查询用户
     *
     * @param email 用户邮箱
     * @return 用户实体
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 插入用户
     *
     * @param user 用户实体
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}