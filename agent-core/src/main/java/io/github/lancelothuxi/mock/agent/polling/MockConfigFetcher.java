package io.github.lancelothuxi.mock.agent.polling;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

import java.util.List;

public interface MockConfigFetcher {


    /**
     * 获取mock配置
     */
    public List<MockConfig> getMockConfigs();
}
