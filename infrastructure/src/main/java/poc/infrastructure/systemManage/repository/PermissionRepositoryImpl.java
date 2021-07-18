package poc.infrastructure.systemManage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.PermissionSource;
import poc.domain.systemManage.repository.PermissionRepository;
import poc.infrastructure.systemManage.mapper.PermissionSourceMapper;
import poc.infrastructure.systemManage.repository.dao.PermissionDao;

import java.security.Permission;
import java.util.List;

@Component
public class PermissionRepositoryImpl implements PermissionRepository {

    @Autowired
    PermissionDao permissionDao;

    private PermissionSourceMapper permissionSourceMapper;
    public PermissionRepositoryImpl() {
        this.permissionSourceMapper = PermissionSourceMapper.MAPPER;
    }

    @Override
    public List<PermissionSource> queryAllMenu() {
        List<PermissionSource> oneMenu = permissionDao.queryOneMenu();
        // 一级菜单
        for (PermissionSource menu : oneMenu) {
            menu.setId(menu.getPermissionId());
            menu.setLabel(menu.getPermissionName());

            // 查询二级菜单
            String permissionId = menu.getPermissionId();
            List<PermissionSource> childrenMenu = permissionDao.queryChildrenMenu(permissionId);
            menu.setChildren(childrenMenu);
            // 查询三级菜单
            for (PermissionSource children : childrenMenu) {
                children.setId(children.getPermissionId());
                children.setLabel(children.getPermissionName());

                String childrenPermissionId = children.getPermissionId();
                List<PermissionSource> threeMenu = permissionDao.queryChildrenMenu(childrenPermissionId);
                for (PermissionSource three : threeMenu) {
                    three.setId(three.getPermissionId());
                    three.setLabel(three.getPermissionName());
                }
                children.setChildren(threeMenu);
            }
        }
        return oneMenu;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param permissionId 主键
     * @return 实例对象
     */
    @Override
    public PermissionSource queryById(String permissionId) {
        return null;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<PermissionSource> queryAllByLimit(int offset, int limit) {
        return null;
    }

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public PermissionSource insert(PermissionSource permission) {
        return null;
    }

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public PermissionSource update(PermissionSource permission) {
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param permissionId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String permissionId) {
        return false;
    }

    @Override
    public String getAllPermissionByUserId(String userId) {
        return permissionDao.getAllPermissionByUserId(userId);
    }

    @Override
    public List<PermissionSource> getOneMenu(String permissionType) {
        return permissionSourceMapper.poListToSource(permissionDao.getOneMenu(permissionType));
    }

    @Override
    public List<PermissionSource> queryChildrenMenu(String permissionId) {
        return permissionDao.queryChildrenMenu(permissionId);
    }
}
