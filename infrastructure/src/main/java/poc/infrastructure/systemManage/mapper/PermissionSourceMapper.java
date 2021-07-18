package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.PermissionSource;
import poc.infrastructure.systemManage.po.Permission;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/6/30 13:43
 */
@Mapper
public interface PermissionSourceMapper {
    PermissionSourceMapper MAPPER = Mappers.getMapper(PermissionSourceMapper.class);

    List<PermissionSource> poListToSource(List<Permission> list);
}
