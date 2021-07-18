package poc.infrastructure.systemManage.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * UUID生成
 *
 * @author tangLingHan
 */
public class IdGenerate {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.  32位长度。
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long , 19位长度.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(IdGenerate.uuid());
        }
        System.out.println(randomLong());
//        BigDecimal b1 = new BigDecimal("10.1");
//        BigDecimal b2 = new BigDecimal("20.2");
//        BigDecimal subtract = b1.subtract(b2);
//        BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
//        System.out.println("divide = " + divide);
//        System.out.println("subtract = " + subtract);
    }

}