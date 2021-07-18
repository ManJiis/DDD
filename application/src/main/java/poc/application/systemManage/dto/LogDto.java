package poc.application.systemManage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    /**
    * 日志ID
    */
    private String logId;
    /**
    * 日志类型 00-系统运行日志，01-用户操作日志，02-用户登录日志
    */
    private String logType;
    /**
    * 日志微服务名称
    */
    private String serviceName;
    /**
    * 用户ID
    */
    private String userId;
    /**
    * 日志级别：0-普通记录，1-警告，2-错误
    */
    private String logLevel;
    /**
    * 日志时间
    */
    private Date logCreateTime;
    /**
    * 日志内容
    */
    private String logContent;
    /**
    * 登录IP
    */
    private String logIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;
    private String createId;
    private String updateId;
    private String isDelete;

    private String loginName;
    private String userName;

    // 请求方式
    private String requestMethod;
    // 请求方法
    private String actionMethod;
    // 请求参数
    private String actionParams;
    // 请求路径
    private String actionUrl;
    // 操作浏览器
    private String browser;
    // 操作项
    private String operate;
    // 操作状态
    private String operateStatus;

    private String moduleName;
    private String tel;
}