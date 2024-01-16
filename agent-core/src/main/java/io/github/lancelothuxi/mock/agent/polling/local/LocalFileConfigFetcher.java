package io.github.lancelothuxi.mock.agent.polling.local;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.util.List;

/**
 * @author lancelot
 */
public class LocalFileConfigFetcher implements MockConfigFetcher {

    @Override
    public List<MockConfig> getMockConfigs(String appName) {

        String localConfigFilePath = GlobalConfig.LOCAL_CONFIG_FILE_PATH;
        if(StringUtils.isEmpty(localConfigFilePath)){
            throw new RuntimeException("file not found");
        }

        return null;
    }
}
