package poc.representation.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ManJiis
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class Response<T> {
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回状态编码
     */
    private String code;
    /**
     * 返回信息
     */
    private String message;

    private Response(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    /**
     * 成功模板
     *
     * @param data /
     * @param <T>  /
     * @return /
     */
    public static <T> Response ok(T data) {
        return new Response(data, "0", "");
    }

    /**
     * 错误模板
     *
     * @param message /
     * @return /
     */
    public static Response error(String message) {
        return new Response(null, "-1", message);
    }

    public static <T> Response error(String message, String code) {
        return new Response(null, code, message);
    }

    /**
     * 保存数据模板
     *
     * @param isSuccess /
     * @return /
     */
    public static Response saveSuccess(boolean isSuccess) {
        if (isSuccess) {
            return new Response(null, "0", "保存成功");
        } else {
            return new Response(null, "-1", "保存失败");
        }

    }
}
