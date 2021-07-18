package poc.application.systemManage;

import org.springframework.stereotype.Service;
import poc.application.systemManage.mapper.UserDTtoMapper;
import poc.domain.systemManage.model.*;
import poc.domain.systemManage.repository.RoleRepository;
import poc.domain.systemManage.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class RoleApplicationService {

    private final RoleRepository repository;


    public RoleApplicationService(RoleRepository repository) {
        this.repository = repository;
    }

    public Long insertRole(RoleSource roleSource) {
        return repository.insert(roleSource);
    }

    public QueryResultSource<RoleSource> queryRoleList(String name, Page page) {
        return repository.queryRoleList(name, page);
    }

/*    public Long queryRoleCode(String name) {
        return repository.queryRoleCode(name);
    }*/

/*    public Long updateRoleById(RoleRequestSource user) {
        return repository.updateRoleById(user);
    }*/

    public Long deleteRoleById(String id) {
        return repository.deleteRoleById(id);
    }

//    public List<Integer> queryMenuByRoleId(String roleId) {
    public Map<String, List<Integer>> queryMenuByRoleId(String roleId) {
        return repository.queryMenuByRoleId(roleId);
    }

    public Long updateRolePermissionByRoleId(String roleId, List<String> permissionIds,List<String> halfIdList) {
        return repository.updateRolePermissionByRoleId(roleId, permissionIds,halfIdList);
    }

    public RoleSource queryRoleByRoleId(String roleId) {
        return repository.queryById(roleId);
    }

    public Long updateRoleByRoleId(RoleSource roleSource) {
        return repository.updateRoleByRoleId(roleSource);
    }
}
