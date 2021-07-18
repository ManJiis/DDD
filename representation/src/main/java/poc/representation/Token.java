package poc.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/6/22 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String userName;

    private String loginName;

    private String userId;

    private String permission;

    private String role;

    private String tel;

    private String office;

    private String regionLevel;
    private String county;
    private String city;
}



