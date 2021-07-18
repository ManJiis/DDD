package poc.domain.systemManage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Role)实体类
 *
 * @author
 * @since 2020-06-22 15:03:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSource {

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 备注说明
     */
    private String remark;
    /**
     * 逻辑删除，1逻辑删除，0未删除
     */
    private String isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;
    private String createId;
    private String updateId;

    // 用户是否拥有该角色
    private boolean isHas;
}