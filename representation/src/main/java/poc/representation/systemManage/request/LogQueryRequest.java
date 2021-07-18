package poc.representation.systemManage.request;

import lombok.Data;

@Data
public class LogQueryRequest {

    String startTime;
    String endTime;
    String name;
    // login 登录日志/ operate 操作日志
    String type;

    String userType;
    String moduleName;

    Integer pageNum;
    Integer pageSize;
}
