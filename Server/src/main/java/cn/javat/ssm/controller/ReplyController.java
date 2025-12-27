package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.ReplyService;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class ReplyController {

    private final ReplyService replyService;
    private final UserService userService;

    @Autowired
    public ReplyController(ReplyService replyService, UserService userService) {
        this.replyService = replyService;
        this.userService = userService;
    }

    /**
     * 分页查询留言回复
     *
     * @param messageId 留言ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 回复列表
     */
    @GetMapping("/messages/{messageId}/replies")
    public ResponseEntity<HttpBody> getRepliesByMessageId(
            @PathVariable Long messageId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        
        ServiceResult<Map<String, Object>> result = replyService.getRepliesByMessageId(messageId, pageNum, pageSize);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(result.getData()));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("查询失败"));
    }

    /**
     * 发表回复
     *
     * @param params 请求参数
     * @param token 请求头中的认证令牌
     * @return 发表结果
     */
    @PostMapping("/replies")
    public ResponseEntity<HttpBody> createReply(
            @RequestBody Map<String, Object> params,
            @RequestHeader("Authorization") String token) {
        
        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();
        
        // 参数校验
        Long messageId = ((Number) params.get("messageId")).longValue();
        Long parentId = params.get("parentId") != null ? ((Number) params.get("parentId")).longValue() : null;
        String content = (String) params.get("content");
        
        if (messageId == null || content == null || content.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("参数不能为空"));
        }
        
        ServiceResult<Long> result = replyService.createReply(messageId, parentId, content, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(Map.of("replyId", result.getData())).msg("回复成功"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("回复失败"));
    }

    /**
     * 删除回复
     *
     * @param replyId 回复ID
     * @param token 请求头中的认证令牌
     * @return 删除结果
     */
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<HttpBody> deleteReply(
            @PathVariable Long replyId,
            @RequestHeader("Authorization") String token) {
        
        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();
        
        ServiceResult<Boolean> result = replyService.deleteReply(replyId, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().msg("删除成功"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("回复不存在"));
        } else if (result.getCode() == 403) {
            return ResponseEntity.ok(HttpBody.bad().code(403).msg("无权限删除该回复"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("删除失败"));
    }
}