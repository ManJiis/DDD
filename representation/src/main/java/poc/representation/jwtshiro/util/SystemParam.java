package poc.representation.jwtshiro.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/6/29 17:15
 */
@Component
public class SystemParam {

    public static String salt;

    @Value("${password.salt}")
    public void setSystemId(String salt) {
        SystemParam.salt=salt;
    }
}
