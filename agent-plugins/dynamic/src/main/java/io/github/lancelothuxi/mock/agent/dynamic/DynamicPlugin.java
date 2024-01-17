package io.github.lancelothuxi.mock.agent.dynamic;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Plugin;
import io.github.lancelothuxi.mock.agent.core.Transformer;
import io.github.lancelothuxi.mock.agent.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.lancelothuxi.mock.agent.dynamic.Constant.QUERY_MOCK_CONFIG_LIST_URL;

public class DynamicPlugin implements Plugin {
    @Override
    public List<Transformer> transformers() {
        return build();
    }

    public List<Transformer> build() {

        QueryMockConfigsRequest queryMockConfigsRequest = new QueryMockConfigsRequest();
        queryMockConfigsRequest.setType("dynamic");
        queryMockConfigsRequest.setAppName(GlobalConfig.applicationName);

        //获取动态mock配置
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

        List<Transformer> transformers =new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : groupMethodsByInterface.entrySet()) {

            DynamicInvokeTransformer dynamicInvokeTransformer=new DynamicInvokeTransformer(entry.getKey(),
                    entry.getValue());
            transformers.add(dynamicInvokeTransformer);
        }

        for (MockConfig mockConfig : mockConfigs.getMockConfigs()) {
            mockConfig.setType("dynamic");
            MockConfigRegistry.add(mockConfig);
        }

        return transformers;
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
