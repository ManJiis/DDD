package poc.infrastructure.systemManage.repository.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.UserRequestSource;
import poc.domain.systemManage.model.UserRoleSource;
import poc.domain.systemManage.model.UserSource;
import poc.infrastructure.systemManage.po.Role;
import poc.infrastructure.systemManage.po.User;

import java.util.Date;
import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Component
public interface UserDao {

    UserSource getLoginVerifyCode(@Param("tel") String tel);

    int deleteVerCode();

    UserSource getLoginVerCode(@Param("uuid")String uuid);

    UserSource getRegistVerifyCode(@Param("tel") String tel);

    int insertLoginVerifyCode(@Param("tel") String tel, @Param("verifyCode") String verifyCode, @Param("expireTime") Date expireTime);
    int updateLoginVerifyCode(@Param("tel") String tel, @Param("verifyCode") String verifyCode, @Param("expireTime") Date expireTime);

    int insertRegistVerifyCode(@Param("tel") String tel, @Param("verifyCode") String verifyCode, @Param("expireTime") Date expireTime);
    int updateRegistVerifyCode(@Param("tel") String tel, @Param("verifyCode") String verifyCode, @Param("expireTime") Date expireTime);

    int insertVerCode(@Param("uuid") String uuid, @Param("loginCode") String loginCode, @Param("loginExpireTime") Date loginExpireTime);

    int isExistByTel(@Param("tel") String tel);

    User queryById(Integer userId);

    Long queryLoginName(String name);

    List<User> queryUserList(@Param("name") String name,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("userType")String userType,@Param("userStatus") String userStatus, @Param("pg") Page page);

    Long queryUserListCount(@Param("name") String name,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("userType")String userType,@Param("userStatus") String userStatus);

    Long insert(UserRequestSource user);

    Long updateUserById(UserRequestSource user);

    Long deleteById(String userId);


    List<Role> queryUserRoleByUserId(String userId);

    int deleteUseRoleByUserId(String userId);

    int insertUserRoleByUserId(@Param("userId") String userId, @Param("roleId") String roleId);

    User getInfoByLoginName(@Param("loginName") String loginName);
    User getGZAPPAccountByLoginName(@Param("loginName") String loginName);

    User getInfoByTel(@Param("tel") String tel);
}