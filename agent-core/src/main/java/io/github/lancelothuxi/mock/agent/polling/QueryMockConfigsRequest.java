package io.github.lancelothuxi.mock.agent.polling;


import io.github.lancelothuxi.mock.agent.config.MockConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryMockConfigsRequest {

    private String applicationName;

    private String type;

    private List<MockConfig> mockConfigList = new ArrayList<>();
}
