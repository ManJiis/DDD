package poc.infrastructure.systemManage.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 16:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compiler {
    private Long id;

    private String name;

    private String orgName;

    private String position;

    private String contact;

    private Date createTime;

    private Date exchangeTime;
}
