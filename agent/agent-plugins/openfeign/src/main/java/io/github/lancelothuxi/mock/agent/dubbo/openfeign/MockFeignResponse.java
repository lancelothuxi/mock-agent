package io.github.lancelothuxi.mock.agent.dubbo.openfeign;

import java.io.Serializable;

public class MockFeignResponse implements Serializable {

    private String data;

    private int code;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean success() {
        return code == 0;
    }
}
