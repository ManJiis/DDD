package poc.domain.systemManage.repository;

import poc.domain.systemManage.model.UserRoleSource;

import java.util.List;


public interface UserRoleRepository {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    UserRoleSource queryById(String roleId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserRoleSource> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tXtUserRole 实例对象
     * @return 实例对象
     */
    UserRoleSource insert(UserRoleSource tXtUserRole);

    /**
     * 修改数据
     *
     * @param tXtUserRole 实例对象
     * @return 实例对象
     */
    UserRoleSource update(UserRoleSource tXtUserRole);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    boolean deleteById(String roleId);

}