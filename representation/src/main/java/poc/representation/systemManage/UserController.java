package poc.representation.systemManage;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import poc.application.systemManage.UserApplicationService;
import poc.domain.systemManage.model.Page;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.response.Response;
import poc.representation.systemManage.mapper.UserRequestMapper;
import poc.representation.systemManage.request.UserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;


@Api(value = "系统用户管理")
@RequestMapping("/user")
public class UserController {

    @Value("${password.salt}")
    private String salt;

    private final UserApplicationService userService;

    public UserController(UserApplicationService applicationService) {
        this.userService = applicationService;
    }


    @SystemControllerLog(description = "查看用户列表")
    @GetMapping("/queryUserList")
    @ApiOperation(value = "查询用户列表")
    public Response queryUserLists(String name, String startTime, String endTime, String userType, String userStatus, Integer pageNum, Integer pageSize) {
        return Response.ok(userService.queryUserList(name, startTime, endTime, userType, userStatus, new Page(pageNum, pageSize)));
    }

    @GetMapping("/queryLoginName")
    @ApiOperation(value = "查询登录名是否已存在")
    public Response queryLoginName(String loginName) {
        Long aLong = userService.queryLoginName(loginName);
        if (aLong > 0) {
            return Response.error("该账号已存在!");
        }
        return Response.ok("该账号可用");
    }

    @SystemControllerLog(description = "新增用户")
    @PostMapping("/insertUser")
    @ApiOperation(value = "新增用户")
    public Response insertUserById(UserRequest userRequest) {
//        String encodedPassword = ShiroKit.md5(userRequest.getLoginPassword(), userRequest.getLoginName() + salt);
//        userRequest.setLoginPassword(encodedPassword);
        Long aLong = userService.insertUser(UserRequestMapper.MAPPER.requestToSource(userRequest));
        if (aLong > 0) {
            return Response.ok("新增成功!");
        }
        return Response.error("新增失败,请稍后重试");
    }

    @SystemControllerLog(description = "修改用户信息")
    @PostMapping("/updateUserById")
    @ApiOperation(value = "通过主键修改用户信息")
    public Response updateUserById(UserRequest userRequest) {
        if (StringUtils.isEmpty(userRequest.getUserId())) {
            return Response.error("非法参数!");
        }
        Long aLong = userService.updateUserById(UserRequestMapper.MAPPER.requestToSource(userRequest));
        if (aLong > 0) {
            return Response.ok("修改成功!");
        }
        return Response.error("修改失败,请稍后重试");
    }

    @SystemControllerLog(description = "删除用户")
    @PutMapping("/deleteUserById")
    @ApiOperation(value = "通过主键删除用户")
    public Response deleteUserById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Response.error("非法参数!");
        }
        Long aLong = userService.deleteUserById(userId);
        if (aLong > 0) {
            return Response.ok("删除成功!");
        }
        return Response.error("删除失败,请稍后重试");
    }

    @SystemControllerLog(description = "查看用户所拥有的角色")
    @GetMapping("/queryUserRoleById")
    @ApiOperation(value = "获取用户所拥有的角色")
    public Response queryUserRoleById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Response.error("非法参数!");
        }
        return Response.ok(userService.queryUserRoleById(userId));
    }

    @SystemControllerLog(description = "修改用户所拥有的角色")
    @PostMapping("/updateUserRoleById")
    @ApiOperation(value = "修改用户所拥有的角色")
    public Response updateUserRoleById(String userId, String roleIds) {
        if (StringUtils.isEmpty(userId)) {
            return Response.error("非法参数!");
        }
        String[] split = roleIds.split(",");
        ArrayList<String> roleIdList = new ArrayList<>(Arrays.asList(split));
//        for (int i = 0; i < roleIdList.size(); i++) {
//            String s = roleIdList.get(i);
//            if ("".equals(s)) {
//                roleIdList.remove(i);
//            }
//        }
        //        while(it.hasNext()) {
//            if("".equals(it.next())) {
//                it.remove();
//            }
//        }
        roleIdList.removeIf(""::equals);
        return Response.ok(userService.updateUserRoleById(userId, roleIdList));
    }

    @GetMapping(path = "/querySons")
    @ApiOperation(value = "根据上级代码,查询所有子级", nickname = "querySons", notes = "")
    public Response querySons(HttpServletRequest request, String code) {
        String authorization = request.getHeader("Authorization");
        RestTemplate in = new RestTemplate();
        String url = "http://127.0.0.1/projectStatistic/querySons?code=" + code;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", authorization);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<Response> r2 = in.exchange(url, HttpMethod.GET, requestEntity, Response.class);
        return r2.getBody();
    }

    @GetMapping(path = "/getOfficeInfoList")
    @ApiOperation(value = "查询科室集合", nickname = "getOfficeInfoList", notes = "")
    public Response getOfficeInfoList() {
        return Response.ok(userService.getListInfo());
    }
}