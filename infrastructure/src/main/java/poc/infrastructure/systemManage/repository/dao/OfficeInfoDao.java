package poc.infrastructure.systemManage.repository.dao;

import org.springframework.stereotype.Component;
import poc.infrastructure.systemManage.po.OfficeInfo;

import java.util.List;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/7/31 14:02
 */
@Component
public interface OfficeInfoDao {

    List<OfficeInfo> getListInfo();
}
