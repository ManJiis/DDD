package poc.representation.jwtshiro.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import poc.representation.Token;

import java.util.Date;

/**
 * @author ManJiis
 */
public class JWTUtil {
    private final static Logger logger = LogManager.getLogger(JWTUtil.class);

    public JWTUtil() {
    }

    /**
     * 过期时间30分钟
     */
    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String userInfo, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userInfo", userInfo)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static Token getToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return JSON.parseObject(jwt.getClaim("userInfo").asString(), Token.class);
        } catch (JWTDecodeException e) {
            return new Token();
        }
    }

    /**
     * 生成签名,30min后过期
     *
     * @return 加密的token
     */
    public static String sign(Token token, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //使用秘钥
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        //  建造者模式
        String jwtString = JWT.create()
                .withClaim("userInfo", JSON.toJSONString(token))
                .withExpiresAt(date)
                .sign(algorithm);
        logger.debug(String.format("JWT:%s", jwtString));
        return jwtString;
    }

}
