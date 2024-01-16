package io.github.lancelothuxi.mock.agent.polling.local;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.lancelothuxi.mock.agent.util.Util.readFileToString;

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

        try {
            String content = readFileToString(new File(localConfigFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
