package io.github.lancelothuxi.mock.agent.polling;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<MockConfig> mockConfigs = new ArrayList<>();

    public List<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}
