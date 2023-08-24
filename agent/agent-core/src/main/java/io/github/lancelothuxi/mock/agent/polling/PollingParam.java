package io.github.lancelothuxi.mock.agent.polling;

import java.util.List;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

public class PollingParam {

    private List<MockConfig> mockConfigs;

    public List<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}
