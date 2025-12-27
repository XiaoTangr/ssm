package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.MessageService;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    /**
     * 分页查询留言列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param sortType 排序类型
     * @param keyword  搜索关键词
     * @param token    请求头中的认证令牌
     * @return 留言列表
     */
    @GetMapping
    public ResponseEntity<HttpBody> getMessageList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "hot") String sortType,
            @RequestParam(required = false) String keyword,
            @RequestHeader(value = "Authorization", required = false) String token) {

        System.out.println("pageNum: " + pageNum);
        System.out.println("pageSize: " + pageSize);
        System.out.println("sortType: " + sortType);
        System.out.println("keyword: " + keyword);

        // 获取当前用户信息（如果提供了token）
        User user = null;
        if (token != null && !token.isEmpty()) {
            ServiceResult<User> userResult = userService.getUserByToken(token);
            if (userResult.isSuccess()) {
                user = userResult.getData();
            }
        }

        ServiceResult<Map<String, Object>> result = messageService.getMessageList(pageNum, pageSize, sortType, keyword, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(result.getData()));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("查询失败"));
    }

    /**
     * 获取单条留言详情
     *
     * @param messageId 留言ID
     * @param token     请求头中的认证令牌
     * @return 留言详情
     */
    @GetMapping("/{messageId}")
    public ResponseEntity<HttpBody> getMessageDetail(
            @PathVariable Long messageId,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 获取当前用户信息（如果提供了token）
        User user = null;
        if (token != null && !token.isEmpty()) {
            ServiceResult<User> userResult = userService.getUserByToken(token);
            if (userResult.isSuccess()) {
                user = userResult.getData();
            }
        }

        ServiceResult<Map<String, Object>> result = messageService.getMessageDetail(messageId, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(result.getData()));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("留言不存在"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("查询失败"));
    }

    /**
     * 发表留言
     *
     * @param params 请求参数
     * @param token  请求头中的认证令牌
     * @return 发表结果
     */
    @PostMapping
    public ResponseEntity<HttpBody> createMessage(
            @RequestBody Map<String, Object> params,
            @RequestHeader("Authorization") String token) {

        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();

        String title = (String) params.get("title");
        String content = (String) params.get("content");

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("标题不能为空"));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("内容不能为空"));
        }

        ServiceResult<Long> result = messageService.createMessage(title, content, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().msg("发表成功").data(result.getData()));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("发表失败"));
    }

    /**
     * 编辑留言
     *
     * @param messageId 留言ID
     * @param params    请求参数
     * @param token     请求头中的认证令牌
     * @return 编辑结果
     */
    @PutMapping("/{messageId}")
    public ResponseEntity<HttpBody> updateMessage(
            @PathVariable Long messageId,
            @RequestBody Map<String, Object> params,
            @RequestHeader("Authorization") String token) {

        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();

        String title = (String) params.get("title");
        String content = (String) params.get("content");

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("标题不能为空"));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("内容不能为空"));
        }

        ServiceResult<Long> result = messageService.updateMessage(messageId, title, content, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(Map.of("updateTime", result.getData())).msg("编辑成功"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("留言不存在"));
        } else if (result.getCode() == 403) {
            return ResponseEntity.ok(HttpBody.forbidden().msg("无权限编辑该留言"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("编辑失败"));
    }

    /**
     * 删除留言
     *
     * @param messageId 留言ID
     * @param token     请求头中的认证令牌
     * @return 删除结果
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<HttpBody> deleteMessage(
            @PathVariable Long messageId,
            @RequestHeader("Authorization") String token) {

        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();

        ServiceResult<Boolean> result = messageService.deleteMessage(messageId, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().msg("删除成功"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("留言不存在"));
        } else if (result.getCode() == 403) {
            return ResponseEntity.ok(HttpBody.forbidden().msg("无权限删除该留言"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("删除失败"));
    }

}