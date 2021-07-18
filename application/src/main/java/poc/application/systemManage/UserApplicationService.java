package poc.application.systemManage;

import org.springframework.stereotype.Service;
import poc.application.systemManage.dto.OfficeInfoDto;
import poc.application.systemManage.dto.UserDto;
import poc.application.systemManage.mapper.OfficeInfoDtoMapper;
import poc.application.systemManage.mapper.UserDTtoMapper;
import poc.domain.systemManage.model.*;
import poc.domain.systemManage.repository.UserRepository;

import java.util.Date;
import java.util.List;


@Service
public class UserApplicationService {
    private final UserRepository repository;

    private final UserDTtoMapper mapper;
    private final OfficeInfoDtoMapper officeInfoDtoMapper;

    public UserApplicationService(UserRepository repository) {
        this.repository = repository;
        this.mapper = UserDTtoMapper.MAPPER;
        this.officeInfoDtoMapper =OfficeInfoDtoMapper.MAPPER;
    }

    public QueryResultSource<UserSource> queryUserList(String name,String startTime,String endTime,String userType,String userStatus, Page page) {
        return repository.queryUserList(name,startTime,endTime,userType,userStatus, page);
    }

    public Long queryLoginName(String name) {
        return repository.queryLoginName(name);
    }

    public Long insertUser(UserRequestSource user) {
        if (user.getUserType()==null) user.setUserType("01");
        return repository.insertUser(user);
    }

    public Long updateUserById(UserRequestSource user) {
        return repository.updateUserById(user);
    }

    public Long deleteUserById(String id) {
        return repository.deleteUserById(id);
    }

    public List<RoleSource> queryUserRoleById(String userId) {
        return repository.queryUserRoleById(userId);
    }

    public Long updateUserRoleById(String userId, List<String> roleIds) {
        return repository.updateUserRoleById(userId, roleIds);
    }

    public int insertUserRoleByUserId(String userId, String roleId) {
        return repository.insertUserRoleByUserId(userId, roleId);
    }
    public UserDto getInfoByLoginName(String loginName) {
        return mapper.sourceToDto(repository.getInfoByLoginName(loginName));
    }
    public UserDto getGZAPPAccountByLoginName(String loginName) {
        return mapper.sourceToDto(repository.getGZAPPAccountByLoginName(loginName));
    }

    public UserSource getInfoByTel(String tel) {
        return repository.getInfoByTel(tel);
    }

    public int insertVerifyCode(String codeType,String tel, String verifyCode, Date expireTime) {
        return repository.insertVerifyCode(codeType,tel, verifyCode, expireTime);
    }

    public int insertVerCode(String uuid, String loginCode, Date loginExpireTime) {
        return repository.insertVerCode(uuid, loginCode, loginExpireTime);
    }

    public UserSource getLoginVerCode(String uuid) {
        return repository.getLoginVerCode(uuid);
    }

    public UserSource getVerifyCode(String codeType, String tel) {
        return repository.getVerifyCode(codeType, tel);
    }

    public List<OfficeInfoDto> getListInfo() {
        return officeInfoDtoMapper.sourceListToDto(repository.getListInfo());
    }
}
