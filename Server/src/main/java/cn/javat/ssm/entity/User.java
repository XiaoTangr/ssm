package cn.javat.ssm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * 对应数据库中的user表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户唯一ID
     */
    private Long id;

    /**
     * 用户邮箱（登录账号）
     */
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码（建议MD5/BCrypt加密存储）
     */
    @JsonIgnore
    private String password;

    /**
     * 角色：0-普通用户 1-管理员
     */
    private Integer role;

    /**
     * 状态：-1-停用 0-正常
     */
    private Integer status;

    /**
     * 创建时间戳（毫秒）
     */
    private Long createTime;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer isDeleted;

}