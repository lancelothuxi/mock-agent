package io.github.lancelothuxi.mock.agent.util;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午7:47
 */
public class GenericDemo<T> {

    private String demo;

    private T data;

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
