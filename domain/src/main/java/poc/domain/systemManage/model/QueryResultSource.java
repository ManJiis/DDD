package poc.domain.systemManage.model;

import lombok.*;

import java.util.List;

/**
 * @ProjectName: poc
 * @Package: poc.domain.model
 * @ClassName: QueryResultSource
 * @Author: tangLingHan
 * @CreateDate: 2020/4/8/008 13:59
 * @Description: 查询结果封装对象
 * @Version: 1.0
 */
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryResultSource<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private Long total;
}
