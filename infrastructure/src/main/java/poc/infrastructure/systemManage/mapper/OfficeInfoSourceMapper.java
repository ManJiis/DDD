package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.OfficeInfoSource;
import poc.infrastructure.systemManage.po.OfficeInfo;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/7/31 14:08
 */
@Mapper
public interface OfficeInfoSourceMapper {
    OfficeInfoSourceMapper MAPPER = Mappers.getMapper(OfficeInfoSourceMapper.class);

    List<OfficeInfoSource> poListToSource(List<OfficeInfo> list);
}
