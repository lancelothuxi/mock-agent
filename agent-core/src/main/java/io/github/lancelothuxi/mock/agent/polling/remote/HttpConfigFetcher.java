package io.github.lancelothuxi.mock.agent.polling.remote;

import com.alibaba.fastjson2.JSON;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.util.HttpUtil;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.QueryMockConfigsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.github.lancelothuxi.mock.agent.config.Constant.SYNC_MOCK_CONFIG_LIST_URL;

/**
 * @author lancelot
 */
public class HttpConfigFetcher implements MockConfigFetcher {
    private static final Logger logger = LoggerFactory.getLogger(HttpConfigFetcher.class);
    @Override
    public List<MockConfig> getMockConfigs(QueryMockConfigsRequest mockConfigsRequest) {
        final String content = HttpUtil.sendPostRequest(SYNC_MOCK_CONFIG_LIST_URL,mockConfigsRequest, 3000);
        try {
            if(logger.isDebugEnabled()){
                logger.debug("getMockConfigs return {}",content);
            }
            return JSON.parseArray(content, MockConfig.class);
        } catch (Exception e) {
            logger.error("getMockConfigs failed",e);
            throw new RuntimeException(e);
        }
    }
}
