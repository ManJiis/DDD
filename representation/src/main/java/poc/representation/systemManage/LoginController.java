package poc.representation.systemManage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.application.systemManage.LogApplicationService;
import poc.application.systemManage.PermissionApplicationService;
import poc.application.systemManage.UserApplicationService;
import poc.application.systemManage.dto.UserDto;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.UserRequestSource;
import poc.domain.systemManage.model.UserSource;
import poc.infrastructure.systemManage.util.HttpUtil;
import poc.infrastructure.systemManage.util.IdWorker;
import poc.infrastructure.systemManage.util.VCodeUtils;
import poc.representation.Token;
import poc.representation.jwtshiro.util.JWTUtil;
import poc.representation.jwtshiro.util.ShiroKit;
import poc.representation.aop.IpUtils;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.response.Response;
import poc.representation.systemManage.mapper.UserRequestMapper;
import poc.representation.systemManage.request.UserRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @Describle:
 * @Author: zhangyifei
 * @since: 2020/4/5 23:03
 */

@Slf4j
@Api(value = "用户登录")
@RequestMapping("/user")
public class LoginController {

    @Value("${password.salt}")
    private String salt;

    @Value("${gzyj.sso.url}")
    private String ssoUrl;

    @Value("${gzyj.oauth.url}")
    private String oauthUrl;

    @Value("${gzyj.ssoCreateSt.url}")
    private String ssoCreateStUrl;
    @Value("${SMSUrl}")
    private String SMSUrl;


    private final LogApplicationService logService;
    private final UserApplicationService userService;
    private final PermissionApplicationService permissionService;

    public LoginController(UserApplicationService userService, PermissionApplicationService permissionService, LogApplicationService logApplicationService) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.logService = logApplicationService;
    }

    @PostMapping(path = "/login")
    @ApiOperation(value = "登录", nickname = "login", notes = "")
    public Response login(String loginName, String password, String appType, HttpServletRequest request) {
        log.info("用户登录");
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)) {
            return Response.error("用户名密码不能为空！", "-1");
        }
        UserDto userInfo = userService.getInfoByLoginName(loginName);
        if (userInfo == null) {
            return Response.error("用户名不存在！", "-1");
        }
        if (!userInfo.getUserStatus().equals("0")) {
            return Response.error("该用户已停用或注销！", "-1");
        }
        // =====================log===================== //
        String ip = IpUtils.getIpAddr(request);
        LogSource logSource = new LogSource();
        logSource.setLogIp(ip);
        logSource.setUserId(String.valueOf(userInfo.getUserId())); // 操作用户ID
        logSource.setUserName(userInfo.getUserName());
        logSource.setLoginName(userInfo.getLoginName());
        logSource.setActionUrl("/user/login");
        logSource.setActionMethod("poc.representation.systemManage.LoginController.login"); //请求方法
        logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
        logSource.setActionParams("loginName" + loginName); //请求参数
        logSource.setOperate("用户登录");
        // =====================log===================== //
        // 从数据库中根据用户名查找该用户信息
        String oldPassword = userInfo.getLoginPassword();

        String permissions = permissionService.getAllPermissionByUserId(userInfo.getUserId().toString());

        String role = "admin";
        Token token = new Token(userInfo.getUserName(), userInfo.getLoginName(), userInfo.getUserId() + "",
                permissions, role, userInfo.getTel(),userInfo.getOffice(),
                userInfo.getRegionLevel(),userInfo.getCounty(),userInfo.getCity());
        // 原密码加密
        String secret = ShiroKit.md5(loginName, loginName + salt);
//        String encodedPassword = ShiroKit.md5(password, loginName + salt);
        if (oldPassword.equals(password)) {
            if (!StringUtils.isEmpty(appType)) {
                List<String> permissionList = Arrays.asList(permissions.split(","));
                if (!permissionList.contains(appType)) {
                    Response.error("用户没有权限使用该app！", "-1");
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("Authorization", JWTUtil.sign(token, secret));
            map.put("menu", permissionService.getMenuJson(permissions, "1"));
            map.put("userName", userInfo.getUserName());
            map.put("loginName", userInfo.getLoginName());
            // 1 省级账号 2 市级账号 3 县区级账号
            map.put("regionLevel",userInfo.getRegionLevel());
            logSource.setOperateStatus("操作成功");
            try {
                this.insertLoginLog(logSource);
            } catch (Exception ignored) {

            }
            return Response.ok(map);
        } else {
            logSource.setOperateStatus("操作失败");
            try {
                this.insertLoginLog(logSource);
            } catch (Exception ignored) {

            }
            return Response.error("用户名或者密码错误！", "-1");
        }
    }

    @GetMapping(path = "/loginCreateSt")
    @ApiOperation(value = "PC端统一登录获取ST", nickname = "loginCreateSt", notes = "")
    public JSONObject loginCreateSt(String userName) {
        String parm = "service=" + userName;
        //统一登录门户校验
        String sendPost = HttpUtil.sendGet(ssoCreateStUrl, parm);
        return JSON.parseObject(sendPost);
    }

    @PostMapping(path = "/login2")
    @ApiOperation(value = "PC端统一登录", nickname = "login2", notes = "")
    public Response login2(String st, HttpServletRequest request) {
        log.info("统一登录");
        String parm = "st=" + st;
        //统一登录门户校验
        String sendPost = HttpUtil.sendPost(ssoUrl, parm);
        log.info("url:{},st:{}", ssoUrl, st);
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(sendPost);
        String username = "";
        Map<String,Object> userVo;
        if (json != null) {
            Integer state = (Integer) json.get("state");
            if (state == 1) {
                username = (String) json.get("username");
                userVo = (Map<String, Object>) json.get("userVo");
            } else {
                return Response.error("凭证无效！", "-1");
            }
        } else {
            return Response.error("凭证无效！", "-1");
        }
//        UserDto userInfo = userService.getInfoByLoginName(userVo.get("username")+"");
        UserDto userInfo = userService.getInfoByLoginName("stuser");
        if (userInfo == null) {
//            UserRequestSource userRequestSource = new UserRequestSource();
//            userRequestSource.setUserType("01");
//            userRequestSource.setLoginName(userVo.get("username")+"");
//            userRequestSource.setLoginPassword("gzhb.123456");
//            userRequestSource.setUserName(userVo.get("name")+"");
//            userRequestSource.setRealName(userVo.get("name")+"");
//            userRequestSource.setDepartment(userVo.get("ssdw")+"");
//            userRequestSource.setPosition(userVo.get("zw")+"");
//            userRequestSource.setRegionLevel("1");
//            userRequestSource.setUserStatus("0");
//            Long userId = userService.insertUser(userRequestSource);
//            userService.insertUserRoleByUserId(userId.toString(),"9");
//            userInfo = userService.getInfoByLoginName("stuser");
            return Response.error("未添加省厅用户！", "-1");
        }
        if (!userInfo.getUserStatus().equals("0")) {
            return Response.error("用户已停用或注销！", "-1");
        }

        // =========log============  //
        String ip = IpUtils.getIpAddr(request);
        LogSource logSource = new LogSource();
        logSource.setLogIp(ip);
        logSource.setUserId(String.valueOf(userInfo.getUserId())); // 操作用户ID
        logSource.setUserName(userInfo.getUserName());
        logSource.setLoginName(userInfo.getLoginName());
        logSource.setActionUrl("/user/login2");
        logSource.setActionMethod("poc.representation.systemManage.LoginController.login2"); //请求方法
        logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
        logSource.setActionParams("loginName" + username); //请求参数
        logSource.setOperate("PC端统一登录");
        logSource.setOperateStatus("操作成功");
        try {
            this.insertLoginLog(logSource);
        } catch (Exception ignored) {

        }
        // =========log============  //

        String permissions = permissionService.getAllPermissionByUserId(userInfo.getUserId().toString());
        String role = "strole";
        Token token = new Token(username, username, userInfo.getUserId() + "",
                permissions, role, userInfo.getTel(),userInfo.getOffice(),
                userInfo.getRegionLevel(),userInfo.getCounty(),userInfo.getCity());
        // 原密码加密
        String secret = ShiroKit.md5(username, username + salt);
        Map<String, Object> map = new HashMap<>();
        map.put("Authorization", JWTUtil.sign(token, secret));
        map.put("menu", permissionService.getMenuJson(permissions, "1"));
        map.put("loginName", username);
        map.put("userName", username);
        // 1 省级账号 2 市级账号 3 县区级账号
        map.put("regionLevel",userInfo.getRegionLevel());
        return Response.ok(map);
    }

    @PostMapping(path = "/login3")
    @ApiOperation(value = "移动端统一登录", nickname = "login3", notes = "")
    public Response login3(String accessToken, HttpServletRequest request) {
        String parm = "access_token=" + accessToken;
        //统一登录门户校验
        String sendPost = HttpUtil.sendGet(oauthUrl, parm);
        Map<String, Object> json;
        try {
            json = JSON.parseObject(sendPost, Map.class);
        } catch (Exception e) {
            return Response.error("凭证无效！", "-1");
        }

        String username = "";
        String loginName = "";

        // =========log============  //
        String ip = IpUtils.getIpAddr(request);
        LogSource logSource = new LogSource();
        logSource.setLogIp(ip);
//        logSource.setUserId(String.valueOf(userInfo.getUserId())); // 操作用户ID
        logSource.setActionUrl("/user/login3");
        logSource.setActionMethod("poc.representation.systemManage.LoginController.login3"); //请求方法
        logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
        logSource.setOperate("移动端统一登录");
        // =========log============  //

        if (json != null && json.get("attributes") != null) {
            List<Map<String, Object>> attributes = (List<Map<String, Object>>) json.get("attributes");
            if (attributes.size() < 1) {
                logSource.setOperateStatus("操作失败");
                try {
                    this.insertLoginLog(logSource);
                } catch (Exception ignored) {

                }
                return Response.error("凭证无效！", "-1");
            }
            for (Map<String, Object> a : attributes) {
                if (a.get("DISPLAYNAME") != null) {
                    username = a.get("DISPLAYNAME").toString();
                    logSource.setUserName(username);
                }
                if (a.get("ACCOUNT") != null) {
                    loginName = a.get("ACCOUNT").toString();
                    logSource.setLoginName(loginName);
                    logSource.setActionParams("loginName" + username); //请求参数
                }
            }
        } else {
            logSource.setOperateStatus("操作失败");
            try {
                this.insertLoginLog(logSource);
            } catch (Exception ignored) {

            }
            return Response.error("凭证无效！", "-1");
        }
        UserDto userInfo = userService.getInfoByLoginName(loginName);
        if (userInfo == null) {
            logSource.setOperateStatus("操作失败");
            try {
                this.insertLoginLog(logSource);
            } catch (Exception ignored) {

            }
            return Response.error("未添加省厅用户！", "-1");
        }
        if (!userInfo.getUserStatus().equals("0")) {
            logSource.setOperateStatus("操作失败");
            try {
                this.insertLoginLog(logSource);
            } catch (Exception ignored) {

            }
            return Response.error("省厅用户已停用或注销！", "-1");
        }
        logSource.setOperateStatus("操作成功");
        try {
            this.insertLoginLog(logSource);
        } catch (Exception ignored) {

        }
        String permissions = permissionService.getAllPermissionByUserId(userInfo.getUserId().toString());
        String role = "strole";
        Token token = new Token(username, username, userInfo.getUserId() + "",
                permissions, role, userInfo.getTel(),userInfo.getOffice(),
                userInfo.getRegionLevel(),userInfo.getCounty(),userInfo.getCity());
        // 原密码加密
        String secret = ShiroKit.md5(loginName, loginName + salt);
        Map<String, Object> map = new HashMap<>();
        map.put("Authorization", JWTUtil.sign(token, secret));
        map.put("menu", permissionService.getMenuJson(permissions, "2"));
        map.put("loginName", loginName);
        map.put("userName", username);
        return Response.ok(map);
    }

    private static final String CODETYPE_LOGIN = "login";
    private static final String CODETYPE_REGIST = "regist";

    // 生成登录验证码
    public String generateVerifyCode(String codeType, String tel) {
        // 验证码
        Long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        String verifyCode = codeStr.substring(codeStr.length() - 6);
        Date date = new Date();
        // 默认十分钟
        Date expireTime = new Date(date.getTime() + 10 * 60 * 1000);
        if (CODETYPE_LOGIN.equals(codeType)) {
            // 登录过期时间：一分钟
            expireTime = new Date(date.getTime() + 60 * 1000);
        } else if (CODETYPE_REGIST.equals(codeType)) {
            // 注册过期时间：十分钟
            expireTime = new Date(date.getTime() + 10 * 60 * 1000);
        }

/*        // 1. 默认注册
        UserSource userInfoByTel = userService.getInfoByTel(tel);
        if (userInfoByTel == null) {
            UserRequest userRequest = new UserRequest();
//            String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
            String userName = tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            userRequest.setLoginName(tel);
            userRequest.setUserName(userName);
            userRequest.setTel(tel);
            userRequest.setRegionLevel("1");
            // 用户状态 0正常 1停用 2注销
            userRequest.setUserStatus("0");
            // 用户类型 01PC端用户 02移动端用户
            userRequest.setUserType("02");
            UserRequestSource userRequestSource = UserRequestMapper.MAPPER.requestToSource(userRequest);
            // 新增用户
            userService.insertUser(userRequestSource);
            // 验证码记录到数据库
            // codeType  login/regist
            userService.insertVerifyCode(codeType, tel, verifyCode, expireTime);
        }*/

        // 验证码记录到数据库
        // codeType  login/regist
        userService.insertVerifyCode(codeType, tel, verifyCode, expireTime);
        return verifyCode;
    }

    @GetMapping(path = "/sendVerifyCode")
    @ApiOperation(value = "发送验证码", nickname = "sendVerifyCode", notes = "")
    // 发送短信
    public Response sendVerifyCode(
            @ApiParam(name = "codeType", value = "验证码类型: login/regist", required = true) String codeType,
            @ApiParam(name = "tel", value = "手机号码", required = true) String tel) {
        if (StringUtils.isBlank(codeType)) {
            return Response.error("非法参数!", "-1");
        }
        if (StringUtils.isBlank(tel)) {
            return Response.error("手机号为空!", "-1");
        }
        if (CODETYPE_LOGIN.equals(codeType)) {
            UserSource userInfoByTel = userService.getInfoByTel(tel);
            if (userInfoByTel == null) {
                return Response.error("该手机号未注册！", "-1");
            }
        } else if (CODETYPE_REGIST.equals(codeType)) {
            UserSource userInfoByTel = userService.getInfoByTel(tel);
            if (userInfoByTel != null) {
                return Response.error("该手机号已注册！", "-1");
            }
        }
        String verifyCode = this.generateVerifyCode(codeType, tel);
        String content = "";
        if (CODETYPE_LOGIN.equals(codeType)) {
            content = "验证码：" + verifyCode + "，一分钟内有效。(如非本人操作，请忽略。)";
        } else if (CODETYPE_REGIST.equals(codeType)) {
            content = "验证码：" + verifyCode + "，十分钟内有效。(如非本人操作，请忽略。)";
        }
//        String url = SMSUrl + "?content=" + content + "+&sjhm=" + tel;
        String url = SMSUrl;
        String param = "content=" + content + "+&sjhm=" + tel;
        // 2. 发送验证码
        String sendPost = HttpUtil.sendPost(url, param);
        Map<String, Object> json;
        try {
            json = JSON.parseObject(sendPost, Map.class);
        } catch (Exception e) {
            return Response.error("凭证无效！", "-1");
        }
        String msg = "";
        if (json != null) {
            msg = (String) json.get("msg");
        }
        if (!msg.equals("000")) {
            return Response.error("短信发送失败!", "-1");
        }
        return Response.ok(verifyCode);
    }

    @GetMapping(path = "/checkVerifyCode")
    @ApiOperation(value = "校验验证码", nickname = "checkVerifyCode", notes = "")
    // 发送短信
    public Response checkVerifyCode(
            @ApiParam(name = "codeType", value = "验证码类型: login/regist", required = true) String codeType,
            String tel,
            String checkCode) {
        if (StringUtils.isBlank(tel) || StringUtils.isBlank(checkCode)) {
            return Response.error("手机号或验证码为空!", "-1");
        }
        UserSource verifyCodeInfo = userService.getVerifyCode(codeType, tel);
        if (CODETYPE_LOGIN.equals(codeType)) {
            Date loginExpireTime = verifyCodeInfo.getLoginExpireTime();
            String loginCode = verifyCodeInfo.getLoginCode();
            Date nowDate = new Date();
            if (loginExpireTime.after(nowDate)) {
                if (!checkCode.equals(loginCode)) {
                    return Response.error("验证码错误", "-1");
                }
            } else {
                return Response.error("验证码已过期", "-1");
            }
        }
        if (CODETYPE_REGIST.equals(codeType)) {
            Date registExpireTime = verifyCodeInfo.getRegistExpireTime();
            String registCode = verifyCodeInfo.getRegistCode();
            Date nowDate = new Date();
            if (registExpireTime.after(nowDate)) {
                if (!checkCode.equals(registCode)) {
                    return Response.error("验证码错误", "-1");
                }
            } else {
                return Response.error("验证码已过期", "-1");
            }
        }

        return Response.error("非法参数!", "-1");
    }


    @PostMapping(path = "/login4")
    @ApiOperation(value = "公众版APP登录", nickname = "login4", notes = "")
    public Response login4(String loginName, String password, String loginType, String tel, String checkCode, HttpServletRequest request) {
        log.info("公众版APP用户登录");
        // 一. 短信登录
        if ("dxdl".equals(loginType)) {
            if (StringUtils.isBlank(tel)) {
                return Response.error("手机号为空！", "-1");
            }
            UserSource userInfoByTel = userService.getInfoByTel(tel);
            if (userInfoByTel == null) {
                return Response.error("该手机号未注册！", "-1");
            }

            // =====================log===================== //
            String ip = IpUtils.getIpAddr(request);
            LogSource logSource = new LogSource();
            logSource.setLogIp(ip);
            logSource.setUserId(String.valueOf(userInfoByTel.getUserId())); // 操作用户ID
            logSource.setActionUrl("/user/login4");
            logSource.setActionMethod("poc.representation.systemManage.LoginController.login4"); //请求方法
            logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
            logSource.setOperate("公众版APP短信登录");
            logSource.setModuleName("登录");
            // =====================log===================== //

            if (StringUtils.isBlank(checkCode)) {
                return Response.error("验证码为空!", "-1");
            }
            String tel1 = userInfoByTel.getTel();
            // 验证码过期时间
            UserSource verifyCode = userService.getVerifyCode(CODETYPE_LOGIN, tel);
            Date loginExpireTime = verifyCode.getLoginExpireTime();
            String loginCode = verifyCode.getLoginCode();
            Date nowDate = new Date();
            // 2. 校验验证码
            if (loginExpireTime.after(nowDate)) {
                if (!tel.equals(tel1) || !checkCode.equals(loginCode)) {
                    return Response.error("手机号或验证码错误!", "-1");
                }
                String role = "admin";
                String permissions = " ";
                Token token = new Token(userInfoByTel.getUserName(), userInfoByTel.getLoginName(), userInfoByTel.getUserId() + "",
                        permissions, role, userInfoByTel.getTel(),userInfoByTel.getOffice(),
                        userInfoByTel.getRegionLevel(),userInfoByTel.getCounty(),userInfoByTel.getCity());
                // 原密码加密
                String secret = ShiroKit.md5(userInfoByTel.getLoginName(), userInfoByTel.getLoginName() + salt);
                Map<String, Object> map = new HashMap<>();
                map.put("Authorization", JWTUtil.sign(token, secret));
                map.put("loginName", userInfoByTel.getLoginName());
                map.put("userName", userInfoByTel.getUserName());
                logSource.setOperateStatus("操作成功");
                this.insertLoginLog(logSource);
                return Response.ok(map);
            } else {
                return Response.error("验证码已过期!", "-1");
            }
        }
        // 二. 账号密码登录 (通过手机号或者登录名登录)
        /*        if ("zhdl".equals(loginType)) {
         *//*            if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)) {
                return Response.error("登录账号或密码为空！", "-1");
            }   *//*
            if (StringUtils.isBlank(loginName)) {
                return Response.error("登录账号为空！", "-1");
            }
            UserDto userInfo = userService.getGZAPPAccountByLoginName(loginName);
            if (userInfo == null) {
                return Response.error("用户名或手机号不存在！", "-1");
            }
            if (!userInfo.getUserStatus().equals("0")) {
                return Response.error("该用户已停用或注销！", "-1");
            }
            String loginName1 = userInfo.getLoginName();
            String tel1 = userInfo.getTel();
            String loginPassword = userInfo.getLoginPassword();
            if (!loginName.equals(loginName1) | !loginName.equals(tel1) || !password.equals(loginPassword)) {
                return Response.error("用户名或密码错误！", "-1");
            }

            // =====================log===================== //
            String ip = IpUtils.getIpAddr(request);
            LogSource logSource = new LogSource();
            logSource.setLogIp(ip);
            logSource.setUserId(String.valueOf(userInfo.getUserId())); // 操作用户ID
            logSource.setActionUrl("/user/login4");
            logSource.setActionMethod("poc.representation.systemManage.LoginController.login4"); //请求方法
            logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
            logSource.setOperate("公众版APP账号密码登录");
            // =====================log===================== //

            String role = "admin";
            String permissions = " ";
            Token token = new Token(userInfo.getUserName(), userInfo.getLoginName(), userInfo.getUserId() + "",
                    permissions, role, userInfo.getTel(),userInfo.getOffice());
            // 原密码加密
            String secret = ShiroKit.md5(userInfo.getLoginName(), userInfo.getLoginName() + salt);
            Map<String, Object> map = new HashMap<>();
            map.put("Authorization", JWTUtil.sign(token, secret));
            map.put("loginName", userInfo.getLoginName());
            map.put("userName", userInfo.getUserName());
            logSource.setOperateStatus("操作成功");
            this.insertLoginLog(logSource);
            return Response.ok(map);
        }*/
        return Response.error("非法参数!", "-1");
    }

    @SystemControllerLog(description = "公众版APP注册")
    @PostMapping("/registAccount")
    @ApiOperation(value = "公众版APP注册")
    public Response registAccount(UserRequest userRequest, HttpServletRequest request) {
        String tel = userRequest.getTel();
        String checkCode = userRequest.getCheckCode();
        if (StringUtils.isBlank(tel) || StringUtils.isBlank(checkCode)) {
            return Response.error("手机号或验证码为空！", "-1");
        }
        UserSource userInfoByTel = userService.getInfoByTel(tel);
        if (userInfoByTel != null) {
            return Response.error("手机号已存在!", "-1");
        }
        // =====================log===================== //
        String ip = IpUtils.getIpAddr(request);
        LogSource logSource = new LogSource();
        logSource.setLogIp(ip);
        logSource.setActionUrl("/user/registAccount");
        logSource.setActionMethod("poc.representation.systemManage.LoginController.registAccount"); //请求方法
        logSource.setBrowser(request.getHeader("User-Agent")); //浏览器/系统版本
        logSource.setOperate("公众版APP账号注册");
        // =====================log===================== //

        UserSource verifyCodeInfo = userService.getVerifyCode(CODETYPE_REGIST, tel);
        Date registExpireTime = verifyCodeInfo.getRegistExpireTime();
        String registCode = verifyCodeInfo.getRegistCode();
        Date nowDate = new Date();
        // 校验验证码是否过期
        if (registExpireTime.after(nowDate)) {
            if (!checkCode.equals(registCode)) {
                return Response.error("验证码错误", "-1");
            }
            String userName = tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            userRequest.setUserName(userName);
            userRequest.setLoginName(tel);
            // 用户类型 01PC端用户 02移动端用户
            userRequest.setUserType("02");
            userRequest.setRegionLevel("1");
            // 用户状态 0正常 1停用 2注销
            userRequest.setUserStatus("0");
            UserRequestSource userRequestSource = UserRequestMapper.MAPPER.requestToSource(userRequest);
            Long aLong = userService.insertUser(userRequestSource);
            if (aLong > 0) {
                logSource.setOperateStatus("操作成功");
                this.insertLoginLog(logSource);
                // 注册后默认登录
                if ("1".equals(userRequest.getIsDefaultLogin())) {
                    String role = "admin";
                    String permissions = " ";
                    Token token = new Token(userRequestSource.getUserName(), userRequestSource.getLoginName(), userRequestSource.getUserId() + "",
                            permissions, role, userRequestSource.getTel(),"","","","");
                    // 原密码加密
                    String secret = ShiroKit.md5(userRequestSource.getLoginName(), userRequestSource.getLoginName() + salt);
                    Map<String, Object> map = new HashMap<>();
                    map.put("Authorization", JWTUtil.sign(token, secret));
                    map.put("loginName", userRequestSource.getLoginName());
                    map.put("userName", userRequestSource.getUserName());
                    return Response.ok(map);
                }
                return Response.ok("注册成功!");
            }
        } else {
            return Response.error("验证码已过期", "-1");
        }
        logSource.setOperateStatus("操作失败");
        this.insertLoginLog(logSource);
        return Response.error("注册失败,请稍后重试", "-1");
    }

    public int insertLoginLog(LogSource logSource) {
        // 日志Id
        long nextId = new IdWorker().nextId();
        String logId = String.valueOf(nextId);
        // 日志对象
        logSource.setLogId(logId);
        logSource.setServiceName("系统管理服务");
        logSource.setLogType("02"); //日志类型 00-系统运行日志，01-用户操作日志，02-用户登录日志
        logSource.setRequestMethod("POST"); //请求方式
        return logService.insertSystemLog(logSource);
    }


    @GetMapping(path = "/loginGetCodeImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "验证码", nickname = "loginGetCodeImg")
    @ApiResponses({
            @ApiResponse(code = 200, response = File.class, message = "")
    })
    public ResponseEntity<InputStream> exportConstProject(HttpServletRequest request,String uuid) {
        VCodeUtils vc = new VCodeUtils();
        InputStream input = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(vc.getImage(), "JPEG", os);
            input = new  ByteArrayInputStream(os.toByteArray());
        }catch (Exception e){
            System.out.println("生成验证码错误！");
        }
        Date date = new Date();
        // 5分钟过期
        Date expireTime = new Date(date.getTime() + 5 * 60 * 1000);
        String code =vc.getText();
        userService.insertVerCode(uuid,code,expireTime);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_GIF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= a.jpg" )
                .body(input);
    }

    @GetMapping(path = "/loginCodCheck")
    @ApiOperation(value = "验证码校验", nickname = "loginCodCheck")
    public Response verify(HttpServletRequest request,String vCode,String uuid){
        UserSource user = userService.getLoginVerCode(uuid);
        if (user==null){
            return Response.error("验证过期。");
        }
        if(user.getLoginExpireTime().after(new Date())){
            //忽略大小写比较
            boolean flag = vCode.equalsIgnoreCase(user.getLoginCode());
            if (flag){
                request.removeAttribute(uuid);
                return Response.ok("验证成功。");
            }else {
                return Response.error("验证不一致。");
            }
        }else{
            return Response.error("验证过期。");
        }


    }

//    @GetMapping(path = "/loginTest2")
//    @ApiOperation(value = "验证码2", nickname = "loginTest2")
//    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response)  {
//        VCodeUtils vc = new VCodeUtils();
//        try {
//            //保存图片并输出到页面
//            vc.saveImage(vc.getImage(), response.getOutputStream());
//            String sb = vc.getText();
//            request.getSession().setAttribute("uuid", sb);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
