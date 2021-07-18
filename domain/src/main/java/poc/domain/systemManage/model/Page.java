package poc.domain.systemManage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Page {
    //页数
    private Integer pageNum;
    //分页大小
    private Integer pageSize;
    //起始位置
    private Integer start;
    //结束位置
    private Integer end;

    public Page(int pageNum, int pageSize) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.start = (pageNum - 1) * pageSize;
        this.end = pageNum * pageSize;
    }

}
