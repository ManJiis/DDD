package poc.infrastructure.systemManage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.*;
import poc.domain.systemManage.repository.RoleRepository;
import poc.infrastructure.systemManage.mapper.RoleSourceMapper;
import poc.infrastructure.systemManage.po.Role;
import poc.infrastructure.systemManage.repository.dao.PermissionDao;
import poc.infrastructure.systemManage.repository.dao.RoleDao;
import poc.infrastructure.systemManage.repository.dao.UserRoleDao;

import java.util.*;


@Component
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    RoleDao roleDao;
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    UserRoleDao userRoleDao;

    private final RoleSourceMapper roleSourceMapper;

    public RoleRepositoryImpl() {
        this.roleSourceMapper = RoleSourceMapper.MAPPER;

    }

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    @Override
    public RoleSource queryById(String roleId) {
        return roleSourceMapper.poToSource(roleDao.queryById(roleId));
    }


    @Override
    public QueryResultSource<RoleSource> queryRoleList(String name, Page page) {
        List<Role> roleList = roleDao.queryRoleList(name, page);
        Long count = roleDao.queryRoleListCount(name);
        List<RoleSource> sourceList = roleSourceMapper.poListToSource(roleList);
        QueryResultSource<RoleSource> resultSource = new QueryResultSource<>();
        resultSource.setList(sourceList);
        resultSource.setTotal(count);
        return resultSource;
    }

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Override
    public Long insert(RoleSource role) {
        role.setCreateTime(new Date());
        return roleDao.insert(role);
    }


    @Override
    public Long updateRoleByRoleId(RoleSource role) {
        role.setExchangeTime(new Date());
        return roleDao.update(role);
    }

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    @Override
    public Long deleteRoleById(String roleId) {
        return roleDao.deleteById(roleId);
    }

    @Override
//    public List<Integer> queryMenuByRoleId(String roleId) {
    public Map<String, List<Integer>> queryMenuByRoleId(String roleId) {
//        List<UserRoleSource> userRoleSources = userRoleDao.queryUserRoleByRoleId(roleId);
        // 根据用户Id查询角色id
//        for (UserRoleSource userRole : userRoleSources) {
//            String roleId = userRole.getRoleId();
        // 根据角色id查询一级菜单
       /* List<PermissionSource> oneMenu = permissionDao.queryOneMenuByRoleId(roleId);
        // 根据角色Id查询二级菜单
        for (PermissionSource menu : oneMenu) {
            menu.setId(menu.getPermissionId());
            menu.setLabel(menu.getPermissionName());

            // 查询二级菜单
            String permissionId = menu.getPermissionId();
            List<PermissionSource> childrenMenu = permissionDao.queryChildrenMenuByRoleId(permissionId, roleId);
            menu.setChildren(childrenMenu);
            // 查询三级菜单
            for (PermissionSource children : childrenMenu) {
                children.setId(children.getPermissionId());
                children.setLabel(children.getPermissionName());

                String childrenPermissionId = children.getPermissionId();
                List<PermissionSource> threeMenu = permissionDao.queryChildrenMenuByRoleId(childrenPermissionId, roleId);
                children.setChildren(threeMenu);

                for (PermissionSource three : threeMenu) {
                    three.setId(three.getPermissionId());
                    three.setLabel(three.getPermissionName());
                }
            }
        }
        // 去重
        Set<PermissionSource> MenuSource = new HashSet<PermissionSource>(oneMenu);
        return new ArrayList<>(MenuSource);*/
//        }
//        return new ArrayList<>();

        List<Integer> selectMenuIdList = roleDao.querySelectMenuIdByRoleId(roleId);
        List<Integer> halfSelectMenuIdList = roleDao.queryHalfSelectMenuIdByRoleId(roleId);
//        return roleDao.queryMenuIdByRoleId(roleId);
        Map<String, List<Integer>> listMap = new HashMap<>();
        listMap.put("selectList", selectMenuIdList);
        listMap.put("halfSelectList", halfSelectMenuIdList);
        return listMap;
    }


    @Override
    public Long updateRolePermissionByRoleId(String roleId, List<String> permissionIds, List<String> halfIds) {
        // 删除角色旧权限
        int aLong = roleDao.deleteRolePermissionByRoleId(roleId);
        for (String permissionId : permissionIds) {
            // 设置角色与全选菜单关联
            roleDao.insertRolePermissionByRoleId(roleId, permissionId);
        }
        for (String halfId : halfIds) {
            // 设置角色与半选菜单关联
            roleDao.insertRPHalfByRoleId(roleId, halfId);
        }
        return 1L;
    }
}
