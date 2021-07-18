package poc.domain.systemManage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/7/31 13:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeInfoSource {
    private Integer id;

    private String officeName;

    private String communicateAddr;

    private String zipcode;

    private String fax;
}
