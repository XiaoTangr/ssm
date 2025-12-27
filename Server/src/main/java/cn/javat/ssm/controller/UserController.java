package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登录
     *
     * @param params 参数列表
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<HttpBody> login(@RequestBody Map<String, Object> params) {
        ServiceResult<User> loginRes = null;

        String email = (String) params.get("email");
        String password = (String) params.get("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("邮箱或密码不能为空"));
        }

        loginRes = userService.login(email, password);

        if (!loginRes.isSuccess()) {
            return switch (loginRes.getCode()) {
                case -2 -> ResponseEntity.ok(HttpBody.bad().msg("用户不存在"));
                case -1 -> ResponseEntity.ok(HttpBody.bad().msg("邮箱或密码错误"));
                default -> ResponseEntity.ok(HttpBody.bad().msg("登录失败"));
            };
        }
        // 构建 token
        Long uid = loginRes.getData().getId();
        String uem = loginRes.getData().getEmail();
        String token = "Bearer " + jwtUtil.generateToken(uid, uem);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return ResponseEntity.ok().headers(headers).body(HttpBody.ok().data(loginRes.getData()));
    }

    /**
     * 注册
     *
     * @param params 登录参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<HttpBody> register(@RequestBody Map<String, Object> params) {
        String email = (String) params.get("email");
        String nickname = (String) params.get("nickName");
        String password = (String) params.get("password");
        String confirmPassword = (String) params.get("confirmPassword");


        System.out.println("email: " + email);
        System.out.println("nickname: " + nickname);
        System.out.println("password: " + password);
        System.out.println("confirmPassword: " + confirmPassword);
//        参数验证
//        验证邮箱
        if (email == null || email.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("邮箱不能为空"));
        }
//        验证昵称
        if (nickname == null || nickname.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("昵称不能为空"));
        }
//        验证密码
        if (password == null || password.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("密码不能为空"));
        }
//        验证确认密码

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("确认密码不能为空"));
        }

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.ok(HttpBody.bad().msg("密码不一致"));
        }

        ServiceResult<User> registerRes = userService.register(email, nickname, password);
        if (!registerRes.isSuccess()) {
            return switch (registerRes.getCode()) {
                case -1 -> ResponseEntity.ok(HttpBody.bad().msg("用户已存在"));
                case -2 -> ResponseEntity.ok(HttpBody.bad().msg("服务器内部错误"));
                default -> ResponseEntity.ok(HttpBody.bad().msg("注册失败"));
            };
        }
        return ResponseEntity.ok(HttpBody.ok().data(registerRes.getData()));
    }


    /**
     * 获取当前用户
     *
     * @param token 令牌
     * @return 当前用户
     */
    @GetMapping("/current")
    public ResponseEntity<HttpBody> getCurrentUser(@RequestHeader(value = "Authorization", required = false, defaultValue = "") String token) {
        ServiceResult<User> user = userService.getUserByToken(token);

        if (!user.isSuccess()) {
            switch (user.getCode()) {
                case -1 -> {
                    return ResponseEntity.ok(HttpBody.bad().code(400).msg("token验证失败"));
                }
                case -2 -> {
                    return ResponseEntity.ok(HttpBody.bad().msg("token不存在"));
                }
                default -> {
                    return ResponseEntity.ok(HttpBody.innerError());
                }
            }
        }
        return ResponseEntity.ok(HttpBody.ok().data(user.getData()));
    }


    @PostMapping("/reset")
    public ResponseEntity<HttpBody> resetPassword(@RequestBody Map<String, Object> params) {
        String nickName = (String) params.get("nickName");
        String email = (String) params.get("email");
        String password = (String) params.get("password");
        String confirmPassword = (String) params.get("confirmPassword");

        if (nickName == null || nickName.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("昵称不能为空"));
        }
        if (email == null || email.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("邮箱不能为空"));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("密码不能为空"));
        }
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("确认密码不能为空"));
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.ok(HttpBody.bad().msg("密码不一致"));
        }
        ServiceResult<User> result = userService.resetPassword(email, nickName, password);

        if (!result.isSuccess()) {
            return switch (result.getCode()) {
                case -1 -> ResponseEntity.ok(HttpBody.bad().msg("邮箱或昵称错误"));
                case -2 -> ResponseEntity.ok(HttpBody.bad().msg("用户不存在"));
                case -3 -> ResponseEntity.ok(HttpBody.bad().msg("服务器内部错误"));
                default -> ResponseEntity.ok(HttpBody.bad().msg("重置密码失败"));
            };
        }
        return ResponseEntity.ok(HttpBody.ok().data(result.getData()));
    }
}