package poc.domain.systemManage.repository;


import poc.domain.systemManage.model.PermissionSource;

import java.security.Permission;
import java.util.List;


public interface PermissionRepository {

    /**
     * 通过ID查询单条数据
     *
     * @param permissionId 主键
     * @return 实例对象
     */
    PermissionSource queryById(String permissionId);


    List<PermissionSource> queryAllMenu();

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<PermissionSource> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    PermissionSource insert(PermissionSource permission);

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    PermissionSource update(PermissionSource permission);

    /**
     * 通过主键删除数据
     *
     * @param permissionId 主键
     * @return 是否成功
     */
    boolean deleteById(String permissionId);

    String getAllPermissionByUserId(String userId);

    List<PermissionSource> getOneMenu(String permissionType);

    List<PermissionSource> queryChildrenMenu(String permissionId);
}