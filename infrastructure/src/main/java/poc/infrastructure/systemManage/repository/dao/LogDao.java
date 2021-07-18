package poc.infrastructure.systemManage.repository.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.LogQueryRequestSource;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.Page;
import poc.infrastructure.systemManage.po.Log;

import java.util.List;
import java.util.Map;

/**
 * (Log)表数据库访问层
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Component
public interface LogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param logId 主键
     * @return 实例对象
     */
    Log queryById(String logId);


    // 查询登录日志
    List<Log> queryLoginLog(@Param("rs") LogQueryRequestSource requestSource, @Param("pg") Page page);

    Long queryLoginLogCount(@Param("rs") LogQueryRequestSource requestSource);

    // 查询操作日志
    List<Log> queryOperateLog(@Param("rs") LogQueryRequestSource requestSource, @Param("pg") Page page);

    Long queryOperateLogCount(@Param("rs") LogQueryRequestSource requestSource);


    /**
     * 新增数据
     *
     * @param log 实例对象
     * @return 影响行数
     */
    int insert(LogSource log);

    /**
     * 修改数据
     *
     * @param log 实例对象
     * @return 影响行数
     */
    int update(Log log);

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 影响行数
     */
    int deleteById(String logId);

    List<Log> getAppLog(@Param("rs") LogQueryRequestSource requestSource, @Param("pg") Page page);

    Long getAppLogCount(@Param("rs") LogQueryRequestSource requestSource);

    List<Map<String,Object>> statLoginLog(@Param("year")String year,@Param("month")String month);
}