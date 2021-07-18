package poc.infrastructure.systemManage.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    /**
    * 权限ID
    */
    private String permissionId;
    /**
    * 权限名称
    */
    private String permissionName;
    /**
    * 权限类型
    */
    private String permissionType;
    /**
    * 权限url
    */
    private String permissionUrl;
    /**
    * 权限代码
    */
    private String permissionCode;
    /**
    * 图标
    */
    private String icon;
    /**
    * 权限父ID
    */
    private String fatherId;
    /**
    * 权限级别
    */
    private String permissionLevel;
    /**
    * 逻辑删除，1逻辑删除，0未删除
    */
    private String isDelete;


}