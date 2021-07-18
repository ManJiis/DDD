package poc.infrastructure.systemManage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.domain.systemManage.model.CompilerSource;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.QueryResultSource;
import poc.domain.systemManage.repository.CompilerRepository;
import poc.infrastructure.systemManage.mapper.CompilerSourceMapper;
import poc.infrastructure.systemManage.mapper.LogSourceMapper;
import poc.infrastructure.systemManage.repository.dao.CompilerDao;
import poc.infrastructure.systemManage.repository.dao.LogDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/4 8:57
 */
@Component
public class CompilerRepositoryImpl implements CompilerRepository {

    @Autowired
    private CompilerDao compilerDao;

    private final CompilerSourceMapper compilerSourceMapper;

    public CompilerRepositoryImpl(){
        this.compilerSourceMapper = CompilerSourceMapper.MAPPER;
    }

    @Override
    public int insert(CompilerSource compiler) {
        return compilerDao.insert(compilerSourceMapper.sourceToPo(compiler));
    }

    @Override
    public int updateById(CompilerSource compiler) {
        return compilerDao.updateById(compilerSourceMapper.sourceToPo(compiler));
    }

    @Override
    public int deleteById(String id) {
        return compilerDao.deleteById(id);
    }

    @Override
    public QueryResultSource<CompilerSource> getList(String name, String orgName, Page page) {
        QueryResultSource<CompilerSource> qrs = new QueryResultSource<>();
        Long count = compilerDao.getListCount(name,orgName);
        qrs.setTotal(count);
        if (count>0){
            qrs.setList(compilerSourceMapper.poListToSource(compilerDao.getList(name, orgName, page)));
        }else {
            qrs.setList(new ArrayList<>());
        }
        return qrs;
    }

}
