package poc.application.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.application.systemManage.dto.OfficeInfoDto;
import poc.domain.systemManage.model.OfficeInfoSource;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/7/31 14:10
 */
@Mapper
public interface OfficeInfoDtoMapper {
    OfficeInfoDtoMapper MAPPER = Mappers.getMapper(OfficeInfoDtoMapper.class);

    List<OfficeInfoDto> sourceListToDto(List<OfficeInfoSource> list);
}
