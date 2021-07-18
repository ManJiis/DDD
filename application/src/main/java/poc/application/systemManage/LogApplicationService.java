package poc.application.systemManage;

import org.springframework.stereotype.Service;
import poc.domain.systemManage.model.LogQueryRequestSource;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.QueryResultSource;
import poc.domain.systemManage.repository.LogRepository;
import poc.infrastructure.systemManage.po.Log;

import java.util.*;

@Service
public class LogApplicationService {
    private final LogRepository repository;

    public LogApplicationService(LogRepository logRepository) {
        this.repository = logRepository;
    }

    public int insertSystemLog(LogSource logSource) {
        return repository.insert(logSource);
    }

    public QueryResultSource<LogSource> querySystemLog(LogQueryRequestSource queryRequestSource) {
        return repository.querySystemLog(queryRequestSource);
    }

    public QueryResultSource<LogSource> getAppLog(LogQueryRequestSource requestSource) {
        return repository.getAppLog(requestSource);
    }


    public Map<String,Object> statLoginLog(String year, String month) {
        List<Map<String,Object>> mapList = repository.statLoginLog(year, month);
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DATE);
        int y = calendar.get(Calendar.YEAR);
        if (y>Integer.valueOf(year)){
            m=12;

        }

        String[] group;
        Integer[] count;
        int index;
        String suffix;
        if (month==null||month.equals("")){
            group = new String[m];
            count = new Integer[m];
            index = m;
            suffix = "月";
        }else {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.YEAR, Integer.valueOf(year));
            calendar2.set(Calendar.MONTH,Integer.valueOf(month)-1);
            if(calendar.after(calendar2)){
                d=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            group = new String[d];
            count = new Integer[d];
            index = d;
            suffix = "号";
        }
        for (int i = 1; i <=index; i++) {
            group[i-1] = i+suffix;
            count[i-1] = 0;
            for (Map<String,Object> map:mapList){
                if (i==Integer.valueOf(map.get("value")+"")){
                    count[i-1] =Integer.valueOf(map.get("value")+"");
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dataAxis",group);
        map.put("data",count);
        return map;
    }
}
