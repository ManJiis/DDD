package poc.infrastructure.systemManage.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.RoleSource;
import poc.infrastructure.systemManage.po.Role;
import poc.infrastructure.systemManage.po.RolePermission;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Component
public interface RoleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    Role queryById(@Param("roleId")String roleId);

    List<Role> queryRoleList(@Param("name") String name, @Param("pg") Page page);

    Long queryRoleListCount(@Param("name") String name);

    List<Role> queryAllRole();

    Long insert(RoleSource role);

    Long update(RoleSource role);

    // 删除 角色 逻辑删除
    Long deleteById(@Param("roleId")String roleId);

    int deleteRolePermissionByRoleId(@Param("id")String id);
    // 设置角色与全选菜单关联
    int insertRolePermissionByRoleId(@Param("roleId")String roleId, @Param("permissionId")String permissionId);
    // 设置角色与半选菜单关联
    int insertRPHalfByRoleId(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    List<Integer> queryMenuIdByRoleId(@Param("roleId")String roleId);

    // 查询全选菜单id
    List<Integer> querySelectMenuIdByRoleId(String roleId);
    // 查询半选菜单ID
    List<Integer> queryHalfSelectMenuIdByRoleId(String roleId);
}