package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @RequestMapping("/hello")
    public HttpBody hello() {
        return HttpBody.ok().msg("hello world").code(200);
    }
}
