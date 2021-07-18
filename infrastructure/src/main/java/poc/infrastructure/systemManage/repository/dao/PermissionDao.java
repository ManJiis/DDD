package poc.infrastructure.systemManage.repository.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.PermissionSource;
import poc.infrastructure.systemManage.po.Permission;

import java.util.List;

/**
 * (Permission)表数据库访问层
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Component
public interface PermissionDao {

    Permission queryById(String permissionId);

    // 查询一级菜单
    List<PermissionSource> queryOneMenu();

    // 根据上级菜单id查询子级菜单
    List<PermissionSource> queryChildrenMenu(String fatherId);

    // 根据角色Id查询一级菜单
    List<PermissionSource> queryOneMenuByRoleId(String roleId);

    // 根据角色Id查询二级菜单
    List<PermissionSource> queryChildrenMenuByRoleId(@Param("fatherId") String fatherId, @Param("roleId") String roleId);
/*
    // 查询二级菜单
    List<PermissionSource> queryTwoMenu();
    // 查询三级菜单
    List<PermissionSource> queryThreeMenu();
*/

    String getAllPermissionByUserId(@Param("userId") String userId);

    List<Permission> getOneMenu(@Param("permissionType")String permissionType);

}