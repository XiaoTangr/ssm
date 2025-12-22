package cn.javat.ssm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密工具类（适配Spring 6.2.7 + JDK 17+）
 */
public class PasswordEncryptUtil {

    private final PasswordEncoder passwordEncoder;

    // 构造方法注入（Spring 6.x推荐构造器注入，禁用字段注入的警告可忽略）
    @Autowired
    public PasswordEncryptUtil(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 加密明文密码
     *
     * @param rawPassword 明文密码（如123456）
     * @return 加密后的密文（含盐值，60位字符串）
     */
    public String encryptPassword(String rawPassword) {
        // JDK 17+可使用Objects.requireNonNull简化空值校验
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("密码不能为空！");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 校验密码是否匹配
     *
     * @param rawPassword     用户输入的明文密码
     * @param encodedPassword 数据库加密密码
     * @return true-匹配，false-不匹配
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}