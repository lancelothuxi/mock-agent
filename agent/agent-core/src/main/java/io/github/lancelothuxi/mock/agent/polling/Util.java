package io.github.lancelothuxi.mock.agent.polling;

import java.util.List;

import com.alibaba.fastjson.JSONPath;

import io.github.lancelothuxi.mock.agent.config.MockData;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/14 下午9:21
 */
public class Util {

    public static MockData getMockData(List<MockData> mockDataList, String argsString) {

        for (MockData mockData : mockDataList) {
            try {
                final Object jsonPathValue = JSONPath.read(argsString, mockData.getMockReqParams());
                if (jsonPathValue == null) {
                    continue;
                }

                if (jsonPathValue.toString().equals(mockData.getExpectedValue())) {
                    return mockData;
                }

            } catch (Exception ex) {
                continue;
            }
        }
        return null;
    }
}
