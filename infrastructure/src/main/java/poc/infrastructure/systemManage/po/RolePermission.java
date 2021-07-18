package poc.infrastructure.systemManage.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {

    /**
    * 角色ID
    */
    private String roleId;
    /**
    * 权限ID
    */
    private String permissionId;



}