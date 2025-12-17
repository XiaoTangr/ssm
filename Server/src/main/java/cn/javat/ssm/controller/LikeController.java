package cn.javat.ssm.controller;

import cn.javat.ssm.common.ApiResponse;
import cn.javat.ssm.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    
    @Autowired
    @Qualifier("likeService")
    private LikeService likeService;
    
    /**
     * 点赞/取消点赞留言
     */
    @PostMapping("/message/{messageId}")
    public ApiResponse likeMessage(@PathVariable Long messageId,
                                  @RequestParam(defaultValue = "false") Boolean isCancel,
                                  HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        int likeCount = likeService.likeMessage(messageId, userId, isCancel);
        if (likeCount >= 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("likeCount", likeCount);
            String message = isCancel ? "取消点赞成功" : "点赞成功";
            return ApiResponse.success(message, data);
        } else {
            return ApiResponse.error(404, "留言不存在");
        }
    }
    
    /**
     * 查询用户点赞状态
     */
    @GetMapping("/message/{messageId}/status")
    public ApiResponse getLikeStatus(@PathVariable Long messageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        boolean isLiked = likeService.isLiked(messageId, userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("isLiked", isLiked);
        
        return ApiResponse.success(data);
    }
}