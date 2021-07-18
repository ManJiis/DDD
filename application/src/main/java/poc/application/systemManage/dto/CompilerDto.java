package poc.application.systemManage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilerDto {
    private Long id;

    private String name;

    private String orgName;

    private String position;

    private String contact;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;
}
