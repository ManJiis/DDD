package poc.application.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.CompilerDto;
import poc.domain.systemManage.model.CompilerSource;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 17:52
 */
@Mapper
public interface CompilerDtoMapper {
    CompilerDtoMapper MAPPER = Mappers.getMapper(CompilerDtoMapper.class);

    List<CompilerDto> sourceListToDto(List<CompilerSource> list);

    CompilerSource dtoToSource(CompilerDto dto);
}
