package io.github.lancelothuxi.mock.agent.polling;

import java.util.ArrayList;
import java.util.List;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

public class Response {

    private List<MockConfig> mockConfigs = new ArrayList<>();

    public List<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}
