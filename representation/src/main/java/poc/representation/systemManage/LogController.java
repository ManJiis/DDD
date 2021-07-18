package poc.representation.systemManage;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.application.systemManage.LogApplicationService;
import poc.domain.systemManage.model.LogSource;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.response.Response;
import poc.representation.systemManage.mapper.LogRequestMapper;
import poc.representation.systemManage.request.LogQueryRequest;


@Api(value = "系统日志")
@RequestMapping("/log")
public class LogController {

    public final LogApplicationService logService;

    public LogController(LogApplicationService applicationService) {
        this.logService = applicationService;
    }

    @PostMapping("/insertSystemLog")
    @ApiOperation(value = "新增日志")
    public Response updateRolePermissionByRoleId(LogSource logSource) {
//        String authorization = request.getHeader("Authorization");
//        long nextId = new IdWorker().nextId();
//        logSource.setLogId(String.valueOf(nextId));
        int i = logService.insertSystemLog(logSource);
        if (i <= 0) {
            return Response.error("新增日志失败");
        }
        return Response.ok("新增日志成功");
    }

    @SystemControllerLog(description = "查看日志")
    @GetMapping("/querySystemLog")
    @ApiOperation(value = "查询日志")
    public Response querySystemLog(LogQueryRequest logQueryRequest) {
        return Response.ok(logService.querySystemLog(LogRequestMapper.MAPPER.requestToSource(logQueryRequest)));
    }

    @GetMapping("/getAppLog")
    @ApiOperation(value = "查询移动端操作日志")
    public Response getAppLog(LogQueryRequest logQueryRequest) {
        return Response.ok(logService.getAppLog(LogRequestMapper.MAPPER.requestToSource(logQueryRequest)));
    }

    @GetMapping("/statLoginLog")
    @ApiOperation(value = "统计登录日志")
    public Response statLoginLog(String year, String month) {
        return Response.ok(logService.statLoginLog(year, month));
    }
}