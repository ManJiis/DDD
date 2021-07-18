package poc.representation.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.UserDto;
import poc.domain.systemManage.model.RoleSource;
import poc.domain.systemManage.model.UserRequestSource;
import poc.domain.systemManage.model.UserSource;
import poc.representation.systemManage.request.RoleRequest;
import poc.representation.systemManage.request.UserRequest;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedSourcePolicy = IGNORE)
public interface RoleRequestMapper {

    RoleRequestMapper MAPPER = Mappers.getMapper(RoleRequestMapper.class);

    RoleSource requestToSource(RoleRequest userRequest);


}
