package io.github.lancelothuxi.mock.agent.util;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/26 下午4:02
 */
public class DemoGeneric<T> {

    private String code;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
