package poc.infrastructure.systemManage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.*;
import poc.domain.systemManage.repository.UserRepository;
import poc.infrastructure.systemManage.mapper.OfficeInfoSourceMapper;
import poc.infrastructure.systemManage.mapper.RoleSourceMapper;
import poc.infrastructure.systemManage.mapper.UserSourceMapper;
import poc.infrastructure.systemManage.po.Role;
import poc.infrastructure.systemManage.po.User;
import poc.infrastructure.systemManage.repository.dao.OfficeInfoDao;
import poc.infrastructure.systemManage.repository.dao.RoleDao;
import poc.infrastructure.systemManage.repository.dao.UserDao;

import java.util.Date;
import java.util.List;


@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private OfficeInfoDao officeInfoDao;

    private final UserSourceMapper userSourceMapper;
    private final RoleSourceMapper roleSourceMapper;
    private final OfficeInfoSourceMapper officeInfoSourceMapper;

    public UserRepositoryImpl() {
        this.userSourceMapper = UserSourceMapper.MAPPER;
        this.roleSourceMapper = RoleSourceMapper.MAPPER;
        this.officeInfoSourceMapper = OfficeInfoSourceMapper.MAPPER;
    }

    @Override
    public UserSource getVerifyCode(String codeType, String tel) {
        if ("login".equals(codeType)) return userDao.getLoginVerifyCode(tel);
        if ("regist".equals(codeType)) return userDao.getRegistVerifyCode(tel);
        return new UserSource();
    }

    @Override
    public int insertVerifyCode(String codeType, String tel, String verifyCode, Date expireTime) {
        int existByTel = userDao.isExistByTel(tel);
        if ("login".equals(codeType)) {
            if (existByTel > 0) {
                return userDao.updateLoginVerifyCode(tel, verifyCode, expireTime);
            }
            return userDao.insertLoginVerifyCode(tel, verifyCode, expireTime);
        }
        if ("regist".equals(codeType)) {
            if (existByTel > 0) {
                return userDao.updateRegistVerifyCode(tel, verifyCode, expireTime);
            }
            return userDao.insertRegistVerifyCode(tel, verifyCode, expireTime);
        }
        return -1;
    }

    @Override
    public int insertVerCode(String uuid, String loginCode, Date loginExpireTime) {
        return userDao.insertVerCode(uuid, loginCode, loginExpireTime);
    }

    @Override
    public UserSource getLoginVerCode(String uuid) {
        return userDao.getLoginVerCode(uuid);
    }


    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public UserSource queryById(Integer userId) {
        return null;
    }

    @Override
    public Long updateUserById(UserRequestSource user) {
        return userDao.updateUserById(user);
    }

    @Override
    public Long deleteUserById(String id) {
        return userDao.deleteById(id);
    }

    @Override
    public QueryResultSource<UserSource> queryUserList(String name,String startTime,String endTime,String userType,String userStatus, Page page) {
        List<User> userList = userDao.queryUserList(name,startTime,endTime,userType,userStatus, page);
        for (User user : userList) {
            if ("0".equals(user.getUserStatus())) {
                user.setUserStatus("正常");
            }
            if ("1".equals(user.getUserStatus())) {
                user.setUserStatus("停用");
            }
            if ("2".equals(user.getUserStatus())) {
                user.setUserStatus("注销");
            }
            String regionLevel = user.getRegionLevel();
            if ("1".equals(regionLevel)){
                user.setCityName("-");
                user.setCountyName("-");
            }
            if ("2".equals(regionLevel)){
                user.setCountyName("-");
            }
        }
        Long count = userDao.queryUserListCount(name,startTime,endTime,userType,userStatus);
        QueryResultSource<UserSource> resultSource = new QueryResultSource<>();
        // 将poList转为sourceList
        List<UserSource> sourceList = userSourceMapper.poListToSource(userList);
        resultSource.setList(sourceList);
        resultSource.setTotal(count);
        return resultSource;
    }

    @Override
    public Long queryLoginName(String name) {
        return userDao.queryLoginName(name);
    }

    @Override
    public Long insertUser(UserRequestSource user) {
        user.setCreateTime(new Date());
        return userDao.insert(user);
    }


    /**
     * 修改数据
     *
     * @param userId 实例对象
     * @return 实例对象
     */
    @Override
    public List<RoleSource> queryUserRoleById(String userId) {
        List<Role> roles = roleDao.queryAllRole();
        List<Role> userRole = userDao.queryUserRoleByUserId(userId);
        for (Role role : roles) {
            // 所有的角色信息
            String roleId = role.getRoleId();
            for (Role role1 : userRole) {
                // 用户拥有的角色信息
                String roleId1 = role1.getRoleId();
                if (roleId.equals(roleId1)) {
                    role.setHas(true);
                }
            }
        }
        return roleSourceMapper.poListToSource(roles);
    }

    @Override
    public Long updateUserRoleById(String userId, List<String> roleIds) {
        userDao.deleteUseRoleByUserId(userId);
        for (String roleId : roleIds) {
            userDao.insertUserRoleByUserId(userId, roleId);
        }
        return 1L;
    }

    @Override
    public int insertUserRoleByUserId(String userId, String roleId) {
        return userDao.insertUserRoleByUserId(userId, roleId);
    }

    @Override
    public UserSource getInfoByLoginName(String loginName) {
        return userSourceMapper.poToSource(userDao.getInfoByLoginName(loginName));
    }

    @Override
    public UserSource getGZAPPAccountByLoginName(String loginName) {
        return userSourceMapper.poToSource(userDao.getGZAPPAccountByLoginName(loginName));
    }

    @Override
    public UserSource getInfoByTel(String tel) {
        return userSourceMapper.poToSource(userDao.getInfoByTel(tel));
    }

    @Override
    public List<OfficeInfoSource> getListInfo() {
        return officeInfoSourceMapper.poListToSource(officeInfoDao.getListInfo());
    }
}
