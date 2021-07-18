package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.UserSource;
import poc.infrastructure.systemManage.po.User;

import java.util.List;

@Mapper
public interface UserSourceMapper {
    UserSourceMapper MAPPER = Mappers.getMapper(UserSourceMapper.class);

    UserSource poToSource(User po);

    List<UserSource> poListToSource(List<User> list);
}
