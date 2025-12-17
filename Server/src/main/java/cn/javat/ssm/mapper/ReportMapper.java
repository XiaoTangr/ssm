package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.Report;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 举报记录Mapper接口
 */
public interface ReportMapper {
    /**
     * 根据ID查询举报记录
     * @param id 举报记录ID
     * @return 举报记录实体
     */
    Report selectById(@Param("id") Long id);
    
    /**
     * 插入举报记录
     * @param report 举报记录实体
     * @return 影响行数
     */
    int insert(Report report);
    
    /**
     * 更新举报记录审核状态
     * @param id 举报记录ID
     * @param auditStatus 审核状态
     * @param auditUserId 审核管理员ID
     * @param auditTime 审核时间
     * @return 影响行数
     */
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus, 
                          @Param("auditUserId") Long auditUserId, @Param("auditTime") Long auditTime);
}