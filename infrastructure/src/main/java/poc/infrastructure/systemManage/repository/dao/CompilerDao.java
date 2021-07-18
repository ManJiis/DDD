package poc.infrastructure.systemManage.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.Page;
import poc.infrastructure.systemManage.po.Compiler;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 16:57
 */
@Component
public interface CompilerDao {

    int insert(Compiler compiler);

    int updateById(Compiler compiler);

    int deleteById(String id);

    List<Compiler> getList(@Param("name")String name,@Param("orgName")String orgName,@Param("pg") Page page);

    Long getListCount(@Param("name")String name,@Param("orgName")String orgName);
}
