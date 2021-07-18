package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.CompilerSource;
import poc.infrastructure.systemManage.po.Compiler;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 17:50
 */
@Mapper
public interface CompilerSourceMapper {
    CompilerSourceMapper MAPPER = Mappers.getMapper(CompilerSourceMapper.class);

    List<CompilerSource> poListToSource(List<Compiler> list);

    Compiler sourceToPo(CompilerSource source);
}
