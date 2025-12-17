package cn.javat.ssm.controller;

import cn.javat.ssm.common.ApiResponse;
import cn.javat.ssm.entity.Reply;
import cn.javat.ssm.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {
    
    @Autowired
    private ReplyService replyService;
    
    /**
     * 分页查询留言回复
     */
    @GetMapping("/message/{messageId}")
    public ApiResponse getRepliesByMessageId(@PathVariable Long messageId,
                                             @RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "20") int pageSize) {
        List<Reply> replies = replyService.getRepliesByMessageId(messageId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", replies);
        // 简化处理，实际应该分页
        result.put("total", replies.size());
        
        return ApiResponse.success(result);
    }
    
    /**
     * 发表回复
     */
    @PostMapping("")
    public ApiResponse postReply(@RequestBody Reply reply, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        reply.setCreatorId(userId);
        reply.setCreateTime(System.currentTimeMillis());
        reply.setIsDeleted(0); // 未删除
        
        boolean result = replyService.postReply(reply);
        if (result) {
            Map<String, Object> data = new HashMap<>();
            data.put("replyId", reply.getId());
            return ApiResponse.success("回复成功", data);
        } else {
            return ApiResponse.error(500, "回复失败");
        }
    }
    
    /**
     * 删除回复
     */
    @DeleteMapping("/{replyId}")
    public ApiResponse deleteReply(@PathVariable Long replyId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        boolean result = replyService.deleteReply(replyId, userId);
        if (result) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error(500, "删除失败");
        }
    }
}