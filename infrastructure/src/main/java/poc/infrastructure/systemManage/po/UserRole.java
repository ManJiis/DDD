package poc.infrastructure.systemManage.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    /**
    * 用户ID
    */
    private String userId;
    /**
    * 角色ID
    */
    private String roleId;


}