package poc.application.systemManage;

import org.springframework.stereotype.Service;
import poc.application.systemManage.dto.CompilerDto;
import poc.application.systemManage.mapper.CompilerDtoMapper;
import poc.domain.systemManage.model.CompilerSource;
import poc.domain.systemManage.model.Page;
import poc.domain.systemManage.model.QueryResultSource;
import poc.domain.systemManage.repository.CompilerRepository;

import java.util.ArrayList;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/4 9:10
 */
@Service
public class CompilerApplicationService {
    private final CompilerRepository compilerRepository;

    private final CompilerDtoMapper compilerDtoMapper;

    public CompilerApplicationService(CompilerRepository compilerRepository){
        this.compilerRepository = compilerRepository;
        this.compilerDtoMapper = CompilerDtoMapper.MAPPER;
    }

    public int insert(CompilerDto compiler) {
        return compilerRepository.insert(compilerDtoMapper.dtoToSource(compiler));
    }

    public int updateById(CompilerDto compiler) {
        return compilerRepository.updateById(compilerDtoMapper.dtoToSource(compiler));
    }

    public int deleteById(String id) {
        return compilerRepository.deleteById(id);
    }

    public QueryResultSource<CompilerSource> getList(String name, String orgName, Page page) {
        return compilerRepository.getList(name, orgName, page);
    }
}
