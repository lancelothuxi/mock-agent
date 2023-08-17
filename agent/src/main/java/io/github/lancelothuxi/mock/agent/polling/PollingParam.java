package io.github.lancelothuxi.mock.agent.polling;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

import java.util.List;

public class PollingParam {

    private List<MockConfig> mockConfigs;

    public List<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}
