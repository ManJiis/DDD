package poc.infrastructure.scheduled;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import poc.infrastructure.systemManage.repository.dao.UserDao;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/16 11:09
 */
@Log4j2
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduledTask {

    @Autowired
    private UserDao userDao;

    //每天定时删除10天前验证码
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteVerCode() {
        userDao.deleteVerCode();
    }
}
