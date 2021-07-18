package poc.domain.systemManage.repository;


import poc.domain.systemManage.model.LogQueryRequestSource;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.QueryResultSource;

import java.util.List;
import java.util.Map;


public interface LogRepository {

    /**
     * 通过ID查询单条数据
     *
     * @param logId 主键
     * @return 实例对象
     */
    LogSource queryById(String logId);


    QueryResultSource<LogSource> querySystemLog(LogQueryRequestSource requestSource);

    /**
     * 新增数据
     *
     * @param log 实例对象
     * @return 实例对象
     */
    int insert(LogSource log);

    /**
     * 修改数据
     *
     * @param log 实例对象
     * @return 实例对象
     */
    LogSource update(LogSource log);

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 是否成功
     */
    boolean deleteById(String logId);


    QueryResultSource<LogSource> getAppLog(LogQueryRequestSource requestSource);

    List<Map<String,Object>> statLoginLog(String year,String month);
}