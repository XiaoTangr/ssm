package cn.javat.ssm.controller;

import cn.javat.ssm.common.HttpBody;
import cn.javat.ssm.common.ServiceResult;
import cn.javat.ssm.entity.User;
import cn.javat.ssm.service.ReportService;
import cn.javat.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    @Autowired
    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    /**
     * 举报留言
     *
     * @param params 请求参数
     * @param token 请求头中的认证令牌
     * @return 举报结果
     */
    @PostMapping("/reports")
    public ResponseEntity<HttpBody> reportMessage(
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
        String reason = (String) params.get("reason");
        
        if (messageId == null || reason == null || reason.isEmpty()) {
            return ResponseEntity.ok(HttpBody.bad().msg("参数不能为空"));
        }
        
        if (reason.length() > 256) {
            return ResponseEntity.ok(HttpBody.bad().msg("举报原因不能超过256个字符"));
        }
        
        ServiceResult<Long> result = reportService.reportMessage(messageId, reason, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(Map.of("reportId", result.getData())).msg("举报成功，等待管理员审核"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("被举报的留言不存在"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("举报失败"));
    }

    /**
     * 管理员查询举报列表
     *
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param auditStatus 审核状态
     * @param token 请求头中的认证令牌
     * @return 举报列表
     */
    @GetMapping("/admin/reports")
    public ResponseEntity<HttpBody> getReports(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "-1") int auditStatus,
            @RequestHeader("Authorization") String token) {
        
        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();
        
        ServiceResult<Map<String, Object>> result = reportService.getReports(pageNum, pageSize, auditStatus, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().data(result.getData()));
        } else if (result.getCode() == 403) {
            return ResponseEntity.ok(HttpBody.bad().code(403).msg("权限不足"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("查询失败"));
    }

    /**
     * 管理员审核举报
     *
     * @param reportId 举报ID
     * @param params 请求参数
     * @param token 请求头中的认证令牌
     * @return 审核结果
     */
    @PutMapping("/admin/reports/{reportId}/audit")
    public ResponseEntity<HttpBody> auditReport(
            @PathVariable Long reportId,
            @RequestBody Map<String, Object> params,
            @RequestHeader("Authorization") String token) {
        
        // 获取当前用户信息
        ServiceResult<User> userResult = userService.getUserByToken(token);
        if (!userResult.isSuccess()) {
            return ResponseEntity.ok(HttpBody.unauthorized().msg("用户未登录"));
        }
        User user = userResult.getData();
        
        // 参数校验
        Integer auditStatus = (Integer) params.get("auditStatus");
        String remark = (String) params.get("remark");
        
        if (auditStatus == null) {
            return ResponseEntity.ok(HttpBody.bad().msg("审核状态不能为空"));
        }
        
        if (auditStatus != 1 && auditStatus != 2) {
            return ResponseEntity.ok(HttpBody.bad().msg("审核状态参数错误"));
        }
        
        ServiceResult<Boolean> result = reportService.auditReport(reportId, auditStatus, remark, user);
        if (result.isSuccess()) {
            return ResponseEntity.ok(HttpBody.ok().msg("审核成功"));
        } else if (result.getCode() == 404) {
            return ResponseEntity.ok(HttpBody.notFound().msg("举报记录不存在"));
        } else if (result.getCode() == 403) {
            return ResponseEntity.ok(HttpBody.bad().code(403).msg("权限不足"));
        }
        return ResponseEntity.ok(HttpBody.bad().msg("审核失败"));
    }
}