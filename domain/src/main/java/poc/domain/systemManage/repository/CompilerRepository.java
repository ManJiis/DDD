package poc.domain.systemManage.repository;

import poc.domain.systemManage.model.CompilerSource;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.QueryResultSource;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 17:25
 */

public interface CompilerRepository {
    int insert(CompilerSource compiler);

    int updateById(CompilerSource compiler);

    int deleteById(String id);

    QueryResultSource<CompilerSource> getList(String name, String orgName, Page page);

}
