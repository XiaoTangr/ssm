package cn.javat.ssm.controller;

import cn.javat.ssm.common.ApiResponse;
import cn.javat.ssm.entity.Message;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.MessageService;
import cn.javat.ssm.service.LikeService;
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 分页查询留言列表
     */
    @GetMapping("")
    public ApiResponse getMessageList(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(required = false) String keyword) {
        List<Message> messages;
        if (keyword != null && !keyword.isEmpty()) {
            messages = messageService.searchMessages(keyword, pageNum, pageSize);
        } else {
            messages = messageService.getMessageList(pageNum, pageSize);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", messages);
        // 简化处理，实际应该查询总数
        result.put("total", messages.size());
        
        return ApiResponse.success(result);
    }
    
    /**
     * 获取单条留言详情
     */
    @GetMapping("/{messageId}")
    public ApiResponse getMessageDetail(@PathVariable Long messageId, HttpServletRequest request) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ApiResponse.error(404, "留言不存在");
        }
        
        // 获取当前用户ID（如果已登录）
        Long currentUserId = (Long) request.getAttribute("userId");
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        
        // 判断当前用户是否点赞
        boolean isLiked = false;
        if (currentUserId != null) {
            isLiked = likeService.isLiked(messageId, currentUserId);
        }
        result.put("isLiked", isLiked);
        
        return ApiResponse.success(result);
    }
    
    /**
     * 发表留言
     */
    @PostMapping("")
    public ApiResponse postMessage(@RequestBody Message message, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        message.setCreatorId(userId);
        message.setCreateTime(System.currentTimeMillis());
        message.setUpdateTime(System.currentTimeMillis());
        message.setStatus(0); // 默认正常状态
        message.setIsDeleted(0); // 未删除
        message.setLikeCount(0); // 初始点赞数为0
        
        boolean result = messageService.postMessage(message);
        if (result) {
            Map<String, Object> data = new HashMap<>();
            data.put("messageId", message.getId());
            return ApiResponse.success("发表成功", data);
        } else {
            return ApiResponse.error(500, "发表失败");
        }
    }
    
    /**
     * 编辑留言
     */
    @PutMapping("/{messageId}")
    public ApiResponse editMessage(@PathVariable Long messageId,
                                   @RequestBody Message updatedMessage,
                                   HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        Message existingMessage = messageService.getMessageById(messageId);
        if (existingMessage == null) {
            return ApiResponse.error(404, "留言不存在");
        }
        
        // 检查权限（创建者才能编辑）
        if (!existingMessage.getCreatorId().equals(userId)) {
            return ApiResponse.forbidden("无权限编辑该留言");
        }
        
        existingMessage.setTitle(updatedMessage.getTitle());
        existingMessage.setContent(updatedMessage.getContent());
        existingMessage.setUpdateTime(System.currentTimeMillis());
        
        boolean result = messageService.editMessage(existingMessage);
        if (result) {
            Map<String, Object> data = new HashMap<>();
            data.put("updateTime", existingMessage.getUpdateTime());
            return ApiResponse.success("编辑成功", data);
        } else {
            return ApiResponse.error(500, "编辑失败");
        }
    }
    
    /**
     * 删除留言
     */
    @DeleteMapping("/{messageId}")
    public ApiResponse deleteMessage(@PathVariable Long messageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        boolean result = messageService.deleteMessage(messageId, userId);
        if (result) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error(500, "删除失败");
        }
    }
}