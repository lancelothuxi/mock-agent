package io.github.lancelothuxi.mock.agent.dynamic;

import io.github.lancelothuxi.mock.agent.config.MockConfig;

import java.util.ArrayList;
import java.util.List;

public class QueryMockConfigsRequest {

    private String appName;

    private String type;

    private List<MockConfig> mockConfigList = new ArrayList<>();

    public List<MockConfig> getMockConfigList() {
        return mockConfigList;
    }

    public void setMockConfigList(List<MockConfig> mockConfigList) {
        this.mockConfigList = mockConfigList;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
