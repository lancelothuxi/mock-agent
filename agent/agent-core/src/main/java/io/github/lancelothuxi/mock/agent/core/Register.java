package io.github.lancelothuxi.mock.agent.core;

import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/23 下午11:34
 */
public interface Register {

    /**
     * @param registry
     */
    void register(MockConfigRegistry registry);
}
