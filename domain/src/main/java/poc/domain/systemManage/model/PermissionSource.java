package poc.domain.systemManage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * (Permission)实体类
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionSource {

    /**
     * 权限ID
     */
    private String permissionId;
    private String id;
    /**
     * 权限名称
     */
    private String permissionName;
    private String label;
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

    private List<PermissionSource> children;

}