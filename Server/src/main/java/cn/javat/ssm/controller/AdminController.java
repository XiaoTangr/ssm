package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 验证管理员权限
     *
     * @param token 请求头中的认证令牌
     * @return 验证结果
     */
    @GetMapping("/verify")
    public ResponseEntity<HttpBody> verifyAdmin(@RequestHeader("Authorization") String token) {
        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();
        
        // 检查是否是管理员
        if (user.getRole() != 1) {
            return ResponseEntity.ok(HttpBody.bad().code(403).msg("权限不足"));
        }
        
        return ResponseEntity.ok(HttpBody.ok().msg("管理员验证成功"));
    }
}