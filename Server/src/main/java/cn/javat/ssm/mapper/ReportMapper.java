package cn.javat.ssm.mapper;

import cn.javat.ssm.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 举报记录Mapper接口
 */
@Mapper
public interface ReportMapper {
    /**
     * 根据ID查询举报记录
     *
     * @param id 举报记录ID
     * @return 举报记录实体
     */
    Report selectById(@Param("id") Long id);

    /**
     * 分页查询举报记录列表
     *
     * @param auditStatus 审核状态
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 举报记录列表
     */
    List<Report> selectList(@Param("auditStatus") Integer auditStatus, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计举报记录总数
     *
     * @param auditStatus 审核状态
     * @return 总数
     */
    int count(@Param("auditStatus") Integer auditStatus);

    /**
     * 插入举报记录
     *
     * @param report 举报记录实体
     * @return 影响行数
     */
    int insert(Report report);

    /**
     * 更新举报记录
     *
     * @param report 举报记录实体
     * @return 影响行数
     */
    int update(Report report);
}