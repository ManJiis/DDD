package poc.representation.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.UserDto;
import poc.domain.systemManage.model.UserRequestSource;
import poc.domain.systemManage.model.UserSource;
import poc.representation.systemManage.request.UserRequest;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedSourcePolicy = IGNORE)
public interface UserRequestMapper {

    UserRequestMapper MAPPER = Mappers.getMapper(UserRequestMapper.class);

    UserRequestSource requestToSource(UserRequest userRequest);

    List<UserDto> sourceListToDto(List<UserSource> userSourceList);


}
