package poc.domain.systemManage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (RolePermission)实体类
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionSource {

    /**
    * 角色ID
    */
    private String roleId;
    /**
    * 权限ID
    */
    private String permissionId;



}