package io.github.lancelothuxi.mock.agent.mock;

import com.alibaba.fastjson.JSONPath;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.express.Express;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.util.CollectionUtils;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.spi.CurrencyNameProvider;

import static io.github.lancelothuxi.mock.agent.polling.Util.getMockData;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午5:19
 */
public abstract class CommonMockService {

    private ConcurrentHashMap<String, Express> expressCache =new ConcurrentHashMap();
    /**
     *
     */
    public Object doMock(String interfaceName, String methodName, String group, String version, Callable supercall,
                         String argsString, Type genericReturnType) throws Exception {

        MockConfig query = new MockConfig();
        query.setInterfaceName(interfaceName);
        query.setMethodName(methodName);
        query.setGroupName(group==null?"":group);
        query.setVersion(version==null?"":version);
        query.setType(getType());

        // try find config from local cache
        final MockConfig mockConfig = MockConfigRegistry.getMockConfig(query);

        if (mockConfig == null) {
            return supercall.call();
        }

        // 本地mock
        final List<MockData> mockDataList = mockConfig.getMockDataList();
        if (CollectionUtils.isEmpty(mockDataList)) {
            throw new RuntimeException("mock agent 获取数据为空或者异常");
        }

        MockData mockData = getMockData(mockDataList, argsString);
        if (mockData == null) {
            throw new RuntimeException("mock agent 获取数据为空或者异常");
        }

        if (mockData.getTimeout() != null && mockData.getTimeout() > 0) {
            Thread.sleep(mockData.getTimeout());
        }

        String data = mockData.getData();

        CompoundVariable compoundVariable = FunctionCache.get(data);
        if (compoundVariable == null) {
            compoundVariable = new CompoundVariable();
            compoundVariable.setParameters(data);
            FunctionCache.putIfAbsent(data, compoundVariable);
        }

        data = compoundVariable.execute(data);

        Object mockValue = ParseUtil.parseMockValue(data, genericReturnType);
        return wrapReturnValue(mockValue);
    }


    protected MockData getMockData(List<MockData> mockDataList, String expression) {

        for (MockData mockData : mockDataList) {
            try {

                if(StringUtils.isEmpty(mockData.getMockReqParams())){
                    return mockData;
                }

                Express express = expressCache.get(expression);
                if(express== null){

                    Object expressValue = express.getValue(mockData, expression);
                }

                final Object jsonPathValue = JSONPath.eval(argsString, mockData.getMockReqParams());
                if (jsonPathValue == null) {
                    continue;
                }

                if (jsonPathValue.toString().equals(mockData.getExpectedValue())) {
                    return mockData;
                }

            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
     * @param returnValue
     * @return
     */
    protected Object wrapReturnValue(Object returnValue) {
        return returnValue;
    }

    protected abstract String getType();
}
