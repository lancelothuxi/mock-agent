package io.github.lancelothuxi.mock.agent.polling;

import java.io.Serializable;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

public class DubboConfigResponse implements Serializable {

    MockConfig dubboMockConfig;

    public DubboConfigResponse() {}

    public DubboConfigResponse(MockConfig dubboMockConfig) {
        this.dubboMockConfig = dubboMockConfig;
    }

    public MockConfig getDubboMockConfig() {
        return dubboMockConfig;
    }

    public void setDubboMockConfig(MockConfig dubboMockConfig) {
        this.dubboMockConfig = dubboMockConfig;
    }
}
