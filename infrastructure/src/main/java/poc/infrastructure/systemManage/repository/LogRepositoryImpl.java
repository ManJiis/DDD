package poc.infrastructure.systemManage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.LogQueryRequestSource;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.QueryResultSource;
import poc.domain.systemManage.repository.LogRepository;
import poc.infrastructure.systemManage.mapper.LogSourceMapper;
import poc.infrastructure.systemManage.po.Log;
import poc.infrastructure.systemManage.repository.dao.LogDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class LogRepositoryImpl implements LogRepository {

    @Autowired
    LogDao logDao;

    private final LogSourceMapper logSourceMapper;

    public LogRepositoryImpl() {
        this.logSourceMapper = LogSourceMapper.MAPPER;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param logId 主键
     * @return 实例对象
     */
    @Override
    public LogSource queryById(String logId) {
        return null;
    }


    @Override
    public QueryResultSource<LogSource> querySystemLog(LogQueryRequestSource requestSource) {
        Page page = new Page(requestSource.getPageNum(), requestSource.getPageSize());
        String type = requestSource.getType();

        if ("login".equals(type)) {
            // 登录日志
            List<Log> loginLog = logDao.queryLoginLog(requestSource, page);
            Long count = logDao.queryLoginLogCount(requestSource);
            QueryResultSource<LogSource> resultSource = new QueryResultSource<>();
            resultSource.setList(logSourceMapper.poListToSource(loginLog));
            resultSource.setTotal(count);
            return resultSource;
        }
        if ("operate".equals(type)) {
            // 操作日志
            List<Log> operateLog = logDao.queryOperateLog(requestSource, page);
            Long count = logDao.queryOperateLogCount(requestSource);
            QueryResultSource<LogSource> resultSource = new QueryResultSource<>();
            resultSource.setList(logSourceMapper.poListToSource(operateLog));
            resultSource.setTotal(count);
            return resultSource;
        }
        QueryResultSource<LogSource> resultSource = new QueryResultSource<>();
        resultSource.setList(new ArrayList<LogSource>());
        resultSource.setTotal(0L);
        return resultSource;
    }

    /**
     * 新增数据
     *
     * @param log 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(LogSource log) {
        log.setLogCreateTime(new Date());
        return logDao.insert(log);
    }

    /**
     * 修改数据
     *
     * @param log 实例对象
     * @return 实例对象
     */
    @Override
    public LogSource update(LogSource log) {
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String logId) {
        return false;
    }

    @Override
    public QueryResultSource<LogSource> getAppLog(LogQueryRequestSource requestSource) {
        Page page = new Page(requestSource.getPageNum(), requestSource.getPageSize());
        QueryResultSource<LogSource> resultSource = new QueryResultSource<>();
        Long count = logDao.getAppLogCount(requestSource);
        resultSource.setTotal(count);
        if (count>0){
            List<Log> loginLog = logDao.getAppLog(requestSource, page);
            resultSource.setList(logSourceMapper.poListToSource(loginLog));
        }else {
            resultSource.setList(new ArrayList<>());
        }
        return resultSource;
    }

    @Override
    public List<Map<String,Object>> statLoginLog(String year, String month) {
        return logDao.statLoginLog(year, month);
    }
}
