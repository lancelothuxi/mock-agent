package io.github.lancelothuxi.mock.agent.polling.local;

import com.alibaba.fastjson2.JSON;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.QueryMockConfigsRequest;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.lancelothuxi.mock.agent.util.ConfigUtil.getPropertyFromEnvOrSystemProperty;
import static io.github.lancelothuxi.mock.agent.util.Util.readFileToString;

/**
 * @author lancelot
 */
public class LocalFileConfigFetcher implements MockConfigFetcher {
    public static  String LOCAL_CONFIG_FILE_PATH ="mock.agent.config.file.path";
    private static final Logger logger = LoggerFactory.getLogger(LocalFileConfigFetcher.class);

    @Override
    public List<MockConfig> getMockConfigs(QueryMockConfigsRequest mockConfigsRequest) {

        String localConfigFilePath = getPropertyFromEnvOrSystemProperty(LOCAL_CONFIG_FILE_PATH);
        if(StringUtils.isEmpty(localConfigFilePath)){
            throw new RuntimeException("file not found");
        }

        try {
            String content = readFileToString(new File(localConfigFilePath));
            if(logger.isDebugEnabled()){
                logger.debug("load config content {}",content);
            }
            return JSON.parseArray(content, MockConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
