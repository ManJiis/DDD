package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.RoleSource;
import poc.domain.systemManage.model.UserSource;
import poc.infrastructure.systemManage.po.Role;
import poc.infrastructure.systemManage.po.User;

import java.util.List;

@Mapper
public interface RoleSourceMapper {
    RoleSourceMapper MAPPER = Mappers.getMapper(RoleSourceMapper.class);

    RoleSource poToSource(Role po);

    List<RoleSource> poListToSource(List<Role> list);
}
