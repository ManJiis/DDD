package poc.domain.systemManage.repository;


import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.PermissionSource;
import poc.domain.systemManage.model.QueryResultSource;
import poc.domain.systemManage.model.RoleSource;

import java.util.List;
import java.util.Map;


public interface RoleRepository {


    RoleSource queryById(String roleId);


    QueryResultSource<RoleSource> queryRoleList(String name, Page page);

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    Long insert(RoleSource role);


    Long updateRoleByRoleId(RoleSource role);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    Long deleteRoleById(String roleId);

    //    List<Integer> queryMenuByRoleId(String id);
    Map<String, List<Integer>> queryMenuByRoleId(String id);

    Long updateRolePermissionByRoleId(String roleId, List<String> permissionIds, List<String> halfIds);
}