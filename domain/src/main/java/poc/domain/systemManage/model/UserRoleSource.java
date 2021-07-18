package poc.domain.systemManage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserRole)实体类
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleSource {

    /**
    * 用户ID
    */
    private String userId;
    /**
    * 角色ID
    */
    private String roleId;


}