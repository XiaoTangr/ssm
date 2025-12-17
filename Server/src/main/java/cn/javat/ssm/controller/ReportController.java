package cn.javat.ssm.controller;

import cn.javat.ssm.common.ApiResponse;
import cn.javat.ssm.entity.Report;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.ReportService;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 举报留言
     */
    @PostMapping("")
    public ApiResponse reportMessage(@RequestBody Report report, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        report.setUserId(userId);
        report.setCreateTime(System.currentTimeMillis());
        report.setAuditStatus(0); // 默认待审核状态
        
        boolean result = reportService.reportMessage(report);
        if (result) {
            Map<String, Object> data = new HashMap<>();
            data.put("reportId", report.getId());
            return ApiResponse.success("举报成功，等待管理员审核", data);
        } else {
            return ApiResponse.error(500, "举报失败");
        }
    }
    
    /**
     * 管理员审核举报
     */
    @PutMapping("/{reportId}/audit")
    public ApiResponse auditReport(@PathVariable Long reportId,
                                 @RequestParam int auditStatus,
                                 HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("请先登录");
        }
        
        // 检查是否为管理员
        User currentUser = userService.getUserById(userId);
        if (currentUser.getRole() != 1) {
            return ApiResponse.forbidden("无权限进行审核操作");
        }
        
        boolean result = reportService.auditReport(reportId, auditStatus, userId);
        if (result) {
            return ApiResponse.success("审核成功");
        } else {
            return ApiResponse.error(500, "审核失败");
        }
    }
}