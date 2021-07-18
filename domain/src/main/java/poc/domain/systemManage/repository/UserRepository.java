package poc.domain.systemManage.repository;


import poc.domain.systemManage.model.*;

import java.util.Date;
import java.util.List;


public interface UserRepository {


    UserSource getVerifyCode(String codeType, String tel);

    int insertVerifyCode(String codeType, String tel, String verifyCode, Date expireTime);

    int insertVerCode(String uuid,String loginCode,Date loginExpireTime);

    UserSource getLoginVerCode(String uuid);

    UserSource queryById(Integer userId);

    // 修改用户信息
    Long updateUserById(UserRequestSource user);

    Long deleteUserById(String id);


    QueryResultSource<UserSource> queryUserList(String name,String startTime,String endTime,String userType,String userStatus, Page page);

    Long queryLoginName(String name);

    Long insertUser(UserRequestSource user);


    /**
     * 修改用户角色关联信息
     *
     * @param userId 实例对象
     * @return 实例对象
     */
    List<RoleSource> queryUserRoleById(String userId);

    Long updateUserRoleById(String userId, List<String> ids);

    int insertUserRoleByUserId(String userId, String roleId);

    UserSource getInfoByLoginName(String loginName);

    UserSource getGZAPPAccountByLoginName(String loginName);

    UserSource getInfoByTel(String tel);

    List<OfficeInfoSource> getListInfo();

}