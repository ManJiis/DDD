package poc.representation.systemManage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import poc.domain.systemManage.model.LogQueryRequestSource;
import poc.representation.systemManage.request.LogQueryRequest;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedSourcePolicy = IGNORE)
public interface LogRequestMapper {

    LogRequestMapper MAPPER = Mappers.getMapper(LogRequestMapper.class);

    LogQueryRequestSource requestToSource(LogQueryRequest logQueryRequest);

    List<LogQueryRequestSource> sourceListToDto(List<LogQueryRequest> logQueryRequests);


}
