package io.github.lancelothuxi.mock.agent.polling.remote;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.util.HttpUtil;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.QueryMockConfigsRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.lancelothuxi.mock.agent.config.Constant.QUERY_MOCK_CONFIG_LIST_URL;
import static io.github.lancelothuxi.mock.agent.util.Util.readFileToString;

/**
 * @author lancelot
 */
public class HttpConfigFetcher implements MockConfigFetcher {

    @Override
    public List<MockConfig> getMockConfigs() {
        QueryMockConfigsRequest queryMockConfigsRequest = new QueryMockConfigsRequest();
        queryMockConfigsRequest.setAppName(GlobalConfig.applicationName);

        final String result = HttpUtil.sendPostRequest(QUERY_MOCK_CONFIG_LIST_URL,queryMockConfigsRequest, 3000);

        try {
            String content = readFileToString(new File(result));
            return com.alibaba.fastjson2.JSON.parseArray(content, MockConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
