package poc.representation.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import poc.application.systemManage.LogApplicationService;
import poc.domain.systemManage.model.LogSource;
import poc.infrastructure.systemManage.util.IdWorker;
import poc.representation.Token;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.jwtshiro.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {
    //注入Service用于把日志保存数据库
    @Autowired
    private final LogApplicationService logService;

    public SystemLogAspect(LogApplicationService applicationService) {
        this.logService = applicationService;
    }

    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);


    //Controller层切点
    @Pointcut("@annotation(poc.representation.aop.annotation.SystemControllerLog)")
    public void controllerAspect() {
    }

/*    @Pointcut("execution( * poc.representation.systemManage.*Controller.*(..))")
    public void controllerLog() {
    }*/

    /**
     * @Description 前置通知  用于拦截Controller层记录用户的操作
     * @since 2020年7月1日
     */
//    @Around("controllerAspect() && controllerLog()")
    @Around("controllerAspect()")
    public Object doBefore(ProceedingJoinPoint pjp) {
//        InvocationContext invocationContext = ContextUtils.getInvocationContext();
//        Invocation context = (Invocation) invocationContext;
//        HttpServletRequestEx request = context.getRequestEx();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            jwt = "";
        }
        Token token = JWTUtil.getToken(jwt);
        String userId = token.getUserId();
        String loginName = token.getLoginName();
        String userName = token.getUserName();
        String ip = IpUtils.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");// 获取系统和浏览器版本
        String requestMethod = request.getMethod();
        String actionUrl = request.getRequestURI();
        String actionMethod = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName();
        String actionMethodName = pjp.getSignature().getName();
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        // 参数名称
        String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        // 参数值
        Object[] args = pjp.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                params += JsonUtils.objectToJson(parameterNames[i] + ":" + args[i]) + ";";
            }
        }
        //*========控制台输出=========*//
        System.out.println("==============通知开始==============");
        System.out.println("请求方式：" + requestMethod); // GET
        System.out.println("请求路径：" + actionUrl); //user/queryUserList
        System.out.println("请求方法：" + actionMethod);// poc.representation.systemManage.UserController.queryUserLists
        System.out.println("请求人Id：" + userId);
        System.out.println("请求ip：" + ip);
        System.out.println("请求参数:" + params);
        System.out.println("浏览器/系统版本:" + userAgent);
        System.out.println("==============通知结束==============");
        //*========数据库日志=========*//
        // 日志Id
        long nextId = new IdWorker().nextId();
        String logId = String.valueOf(nextId);
        // 日志对象
        LogSource log = new LogSource();
        log.setLogId(logId);
        log.setServiceName("系统管理服务");
        if ("login".equals(actionMethodName) | "login2".equals(actionMethodName) | "login3".equals(actionMethodName) | "login4".equals(actionMethodName)) {
            log.setLogType("02"); //日志类型 00-系统运行日志，01-用户操作日志，02-用户登录日志
        } else {
            log.setLogType("01");
        }
        log.setRequestMethod(requestMethod); //请求方式
        log.setActionUrl(actionUrl); //请求路径
        log.setActionMethod(actionMethod); //请求方法
        log.setBrowser(userAgent); //浏览器/系统版本
        log.setActionParams(params); //请求参数
        log.setLogIp(ip); // 登录IP
        log.setUserId(userId); // 操作用户ID
        log.setUserName(userName);
        log.setLoginName(loginName);
        Object result = null;
        try {
            System.out.println("方法描述：" + this.getControllerMethodDescription(pjp));
            String description = this.getControllerMethodDescription(pjp);
            log.setOperate(description); //操作项(请求方法描述)
            String[] split = description.split("--");
            if (split.length >= 3) {
                String moduleName = split[1];
                log.setModuleName(moduleName);
            }
            log.setOperateStatus("操作成功");
            result = pjp.proceed();
            return result;
        } catch (Throwable e) {
            //记录本地异常日志
            logger.error("====通知异常====");
            logger.error("异常信息：{}", e.getMessage());
            log.setOperateStatus("操作失败");
            throw new RuntimeException(e);
        } finally {
            try {
                //保存数据库
                int i = logService.insertSystemLog(log);
                logger.info("logInsertResult = " + (i > 0 ? true : false));
            } catch (Exception e) {

            }
        }
    }


    /**
     * @Description 获取注解中对方法的描述信息 用于Controller层注解
     * @since 2020年7月1日
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}