package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.LikeRecordService;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LikeRecordController {

    private final LikeRecordService likeRecordService;
    private final UserService userService;

    @Autowired
    public LikeRecordController(LikeRecordService likeRecordService, UserService userService) {
        this.likeRecordService = likeRecordService;
        this.userService = userService;
    }

    /**
     * 点赞/取消点赞留言
     *
     * @param messageId 留言ID
     * @param params    请求参数
     * @param token     请求头中的认证令牌
     * @return 点赞结果
     */
    @PostMapping("/messages/{messageId}/like")
    public ResponseEntity<HttpBody> likeMessage(@PathVariable Long messageId, @RequestBody Map<String, Object> params, @RequestHeader("Authorization") String token) {

        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();

        // 获取参数
        Boolean isCancel = (Boolean) params.get("isCancel");

        ServiceResult<Integer> result = likeRecordService.likeMessage(messageId, isCancel, user);
        if (result.isSuccess()) {
            String msg = (isCancel != null && isCancel) ? "取消点赞成功" : "点赞成功";
            return ResponseEntity.ok(HttpBody.ok().data(Map.of("likeCount", result.getData())).msg(msg));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("留言不存在"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("操作失败"));
    }

    /**
     * 查询用户点赞状态
     *
     * @param messageId 留言ID
     * @param token     请求头中的认证令牌
     * @return 点赞状态
     */
    @GetMapping("/messages/{messageId}/like/status")
    public ResponseEntity<HttpBody> getLikeStatus(@PathVariable Long messageId, @RequestHeader("Authorization") String token) {

        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();

        ServiceResult<Boolean> result = likeRecordService.getLikeStatus(messageId, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(Map.of("isLiked", result.getData())).msg("查询成功"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("留言不存在"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("查询失败"));
    }
}