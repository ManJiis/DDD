//package poc.representation.jwtshiro.common.config;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
//import org.apache.servicecomb.core.Const;
//import org.apache.servicecomb.core.Invocation;
//import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
//import org.apache.servicecomb.swagger.engine.SwaggerProducerOperation;
//import org.apache.servicecomb.swagger.invocation.Response;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import poc.representation.Token;
//import poc.representation.jwtshiro.util.JWTUtil;
//import poc.representation.jwtshiro.util.ShiroKit;
//import poc.representation.jwtshiro.util.SystemParam;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class HttpAuthFilter implements HttpServerFilter {
//
//    public HttpAuthFilter() {
//    }
//
//    @Override
//    public int getOrder() {
//        return -10000;  // 确保这个Filter在一般的filter之前先执行
//    }
//
//    @Override
//    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx httpServletRequestEx) {
////        String path = httpServletRequestEx.getPathInfo();
////        String userInfo = httpServletRequestEx.getHeader("Authorization");
////        if (userInfo == null || userInfo.isEmpty() || userInfo.equals("undefined")) {
////            return tryLogin(httpServletRequestEx, path);
////
////        }
////        if (path.startsWith("/") && !path.contains("login") && !path.contains("sendVerifyCode") && !path.contains("registAccount")) { // 只对特定的资源检测
////            Token token = JWTUtil.getToken(userInfo);
////            if (token == null || token.getUserId() == null) {
//////                return null;
////                return Response.create(401, "登录过期,请重新登录！",
////                        poc.representation.response.Response.error("登录过期，请重新登录！", "401"));
////            }
////            String secret = "";
////            try {
////                secret = ShiroKit.md5(token.getLoginName(), token.getLoginName() + SystemParam.salt);
////            } catch (Exception e) {
////                return Response.create(401, "登录过期,请重新登录！",
////                        poc.representation.response.Response.error("登录过期，请重新登录！", "401"));
////            }
////            if (!JWTUtil.verify(userInfo, JSON.toJSONString(token), secret)) {
////                return Response.create(401, "登录过期,请重新登录！",
////                        poc.representation.response.Response.error("登录过期，请重新登录！", "401"));
////            }
////            SwaggerProducerOperation swaggerProducerOperation = invocation.getOperationMeta().getExtData(Const.PRODUCER_OPERATION);
////            RequiresPermissions requiresPermissions = swaggerProducerOperation.getProducerMethod().getAnnotation(RequiresPermissions.class);
//////            RequiresRoles requiresRoles = swaggerProducerOperation.getProducerMethod().getAnnotation(RequiresRoles.class);
////            //角色权限(登录token未存对应信息)
//////                if (requiresRoles!=null){
//////                    //访问需要权限
//////                    String[] roles = requiresRoles.value();
//////                    //用户拥有权限
//////                    List<String> userRoles = Arrays.asList(token.getRole().split(","));
//////                    for (String role: roles){
//////                        if (!userRoles.contains(role)){
//////                            return Response.create(407, "权限不足！",
//////                                    poc.representation.response.Response.error("权限不足！","401"));
//////                        }
//////                    }
//////                }
////            //菜单权限
////            if (requiresPermissions != null) {
////                //访问需要权限
////                String[] permissions = requiresPermissions.value();
////                //用户拥有权限
////                List<String> userPermissions = Arrays.asList(token.getPermission().split(","));
////                for (String permission : permissions) {
////                    if (!userPermissions.contains(permission)) {
////                        return Response.create(407, "权限不足！",
////                                poc.representation.response.Response.error("权限不足！", "401"));
////                    }
////                }
////            }
////        }
//        return null;
//    }
//
////    private Response tryLogin(HttpServletRequestEx httpServletRequestEx, String path) {
////        if (path.contains("login") | path.contains("sendVerifyCode") | path.contains("registAccount")) {
////            return null;
////        }
////        return null;
////        return Response.create(401, "未授权",
////                poc.representation.response.Response.error("未登录用户，请先登录！", "401"));
////    }
//}
