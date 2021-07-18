package poc.application.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.UserDto;
import poc.domain.systemManage.model.UserSource;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedSourcePolicy = IGNORE)
public interface UserDTtoMapper {

    UserDTtoMapper MAPPER = Mappers.getMapper(UserDTtoMapper.class);

    UserDto sourceToDto(UserSource userSource);

    List<UserDto> sourceListToDto(List<UserSource> userSourceList);


}
