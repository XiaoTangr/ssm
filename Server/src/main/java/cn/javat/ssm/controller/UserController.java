package cn.javat.ssm.controller;

import cn.javat.ssm.common.ResponsePayload;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/test")
    public ResponseEntity<ResponsePayload> test() {
        System.out.println("test");
        ResponsePayload responsePayload = ResponsePayload.builder().ok().msg("测试成功");
        System.out.println(responsePayload);
        return ResponseEntity.ok(responsePayload);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload> login(@RequestBody String email, @RequestBody String password) {
        return ResponseEntity.badRequest().body(ResponsePayload.builder().bad());
    }
}