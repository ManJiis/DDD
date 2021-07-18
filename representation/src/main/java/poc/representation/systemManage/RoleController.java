package poc.representation.systemManage;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.application.systemManage.RoleApplicationService;
import poc.domain.systemManage.model.Page;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.response.Response;
import poc.representation.systemManage.mapper.RoleRequestMapper;
import poc.representation.systemManage.request.RoleRequest;

import java.util.ArrayList;
import java.util.Arrays;

@Api(value = "系统角色管理")
@RequestMapping("/role")
public class RoleController {
    private final RoleApplicationService roleService;

    public RoleController(RoleApplicationService applicationService) {
        this.roleService = applicationService;
    }

    @SystemControllerLog(description = "新增角色")
    @PostMapping("/insertRole")
    @ApiOperation(value = "新增角色")
    public Response insertRole(RoleRequest roleRequest) {
        Long i = roleService.insertRole(RoleRequestMapper.MAPPER.requestToSource(roleRequest));
        if (i > 0) {
            return Response.ok("新增成功!");
        }
        return Response.error("新增失败,请稍后重试");
    }

    @SystemControllerLog(description = "查询角色列表")
    @GetMapping("/queryRoleList")
    @ApiOperation(value = "查询角色列表")
    public Response queryRoleList(String name, Integer pageNum, Integer pageSize) {
        return Response.ok(roleService.queryRoleList(name, new Page(pageNum, pageSize)));
    }

    @SystemControllerLog(description = "删除角色")
    @PutMapping("/deleteRoleById")
    @ApiOperation(value = "通过主键删除角色")
    public Response deleteRoleById(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return Response.error("非法参数!");
        }
        Long aLong = roleService.deleteRoleById(roleId);
        if (aLong > 0) {
            return Response.ok("删除成功!");
        }
        return Response.error("删除失败,请稍后重试");
    }

    @GetMapping("/queryMenuByRoleId")
    @ApiOperation(value = "通过角色id获取角色所拥有的权限(菜单)")
    public Response queryMenuByRoleId(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return Response.error("非法参数!");
        }
        return Response.ok(roleService.queryMenuByRoleId(roleId));
    }

    @SystemControllerLog(description = "修改角色权限")
    @PostMapping("/updateRolePermissionByRoleId")
    @ApiOperation(value = "修改角色权限")
    public Response updateRolePermissionByRoleId(String roleId, String permissionIds, String halfSelectIds) {
        if (StringUtils.isEmpty(roleId)) {
            return Response.error("非法参数!");
        }
        // 全选菜单Id
        String[] split = permissionIds.split(",");
        ArrayList<String> permissionList = new ArrayList<>(Arrays.asList(split));
        for (int i = 0; i < permissionList.size(); i++) {
            String s = permissionList.get(i);
            if ("".equals(s)) {
                permissionList.remove(i);
            }
        }
        // 半选菜单ID
        String[] split1 = halfSelectIds.split(",");
        ArrayList<String> halfList = new ArrayList<>(Arrays.asList(split1));
        for (int i = 0; i < halfList.size(); i++) {
            String s = halfList.get(i);
            if ("".equals(s)) {
                halfList.remove(i);
            }
        }
        roleService.updateRolePermissionByRoleId(roleId, permissionList, halfList);
        return Response.ok("修改成功");
    }


    @GetMapping("/queryRoleByRoleId")
    @ApiOperation(value = "通过角色id查询角色基本信息")
    public Response queryRoleByRoleId(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return Response.error("非法参数!");
        }
        return Response.ok(roleService.queryRoleByRoleId(roleId));
    }

    @SystemControllerLog(description = "修改角色基本信息")
    @PostMapping("/updateRoleByRoleId")
    @ApiOperation(value = "修改角色基本信息")
    public Response updateRoleByRoleId(RoleRequest roleRequest) {
        if (StringUtils.isEmpty(roleRequest.getRoleId())) {
            return Response.error("非法参数!");
        }
        Long aLong = roleService.updateRoleByRoleId(RoleRequestMapper.MAPPER.requestToSource(roleRequest));
        if (aLong > 0) {
            return Response.ok("修改成功");
        }
        return Response.error("修改失败,请稍后重试");
    }
}