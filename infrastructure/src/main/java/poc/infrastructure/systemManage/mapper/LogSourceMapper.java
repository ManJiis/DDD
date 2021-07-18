package poc.infrastructure.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.LogSource;
import poc.domain.systemManage.model.UserSource;
import poc.infrastructure.systemManage.po.Log;
import poc.infrastructure.systemManage.po.User;

import java.util.List;

@Mapper
public interface LogSourceMapper {
    LogSourceMapper MAPPER = Mappers.getMapper(LogSourceMapper.class);

    LogSource poToSource(Log po);

    List<LogSource> poListToSource(List<Log> list);
}
