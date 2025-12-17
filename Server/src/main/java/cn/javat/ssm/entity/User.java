package cn.javat.ssm.entity;

import lombok.Data;
import lombok.Getter;

/**
 * 用户实体类
 * 对应数据库中的user表
 */
@Data
public class User {
    // Getters and Setters
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}