package poc.application.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.LogDto;
import poc.domain.systemManage.model.LogSource;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedSourcePolicy = IGNORE)
public interface LogDTtoMapper {

    LogDTtoMapper MAPPER = Mappers.getMapper(LogDTtoMapper.class);

    LogDto sourceToDto(LogSource userSource);

    List<LogDto> sourceListToDto(List<LogSource> userSourceList);


}
