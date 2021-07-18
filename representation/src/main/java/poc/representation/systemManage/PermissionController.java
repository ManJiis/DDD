package poc.representation.systemManage;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.application.systemManage.PermissionApplicationService;
import poc.representation.response.Response;

@Api(value = "系统权限管理")
@RequestMapping("/permission")
public class PermissionController {
    private final PermissionApplicationService permissionService;

    public PermissionController(PermissionApplicationService applicationService) {
        this.permissionService = applicationService;
    }

    @GetMapping("/queryAllMenu")
    @ApiOperation(value = "查询所有菜单列表")
    public Response queryAllMenu() {
        return Response.ok(permissionService.queryAllMenu());
    }
}