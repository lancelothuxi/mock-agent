package io.github.lancelothuxi.mock.agent.dynamic;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.PluginFinder;
import io.github.lancelothuxi.mock.agent.polling.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.lancelothuxi.mock.agent.dynamic.Constant.QUERY_MOCK_CONFIG_LIST_URL;

public class DynamicInvokerBuilder {

    public void build(PluginFinder pluginFinder) {

        try {

            QueryMockConfigsRequest queryMockConfigsRequest = new QueryMockConfigsRequest();
            queryMockConfigsRequest.setType("dynamic");
            queryMockConfigsRequest.setAppName(GlobalConfig.applicationName);

            String result = HttpUtil.sendPostRequest(QUERY_MOCK_CONFIG_LIST_URL, JSON.toJSONString(queryMockConfigsRequest));

            MockConfigListResponse mockConfigs = JSON.parseObject(result, MockConfigListResponse.class);

            Map<String, List<String>> groupMethodsByInterface = new HashMap<>();

            for (MockConfig mockConfig : mockConfigs.getMockConfigs()) {
                if (!groupMethodsByInterface.containsKey(mockConfig.getInterfaceName())) {
                    List<String> methodNames = new ArrayList<>();
                    methodNames.add(mockConfig.getMethodName());
                    groupMethodsByInterface.put(mockConfig.getInterfaceName(), methodNames);
                } else {
                    groupMethodsByInterface.get(mockConfig.getInterfaceName()).add(mockConfig.getMethodName());
                }
            }


            for (Map.Entry<String, List<String>> entry : groupMethodsByInterface.entrySet()) {
                pluginFinder.addTransformer(new DynamicInvokeTransformer(entry.getKey(),
                        entry.getValue()));
            }

            for (MockConfig mockConfig : mockConfigs.getMockConfigs()) {
                mockConfig.setType(Constant.MOCK_TYPE_DYNAMIC);
                MockConfigRegistry.add(mockConfig);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static class MockConfigListResponse{
        private List<MockConfig> mockConfigs = new ArrayList<>();

        public List<MockConfig> getMockConfigs() {
            return mockConfigs;
        }

        public void setMockConfigs(List<MockConfig> mockConfigs) {
            this.mockConfigs = mockConfigs;
        }

    }
}
