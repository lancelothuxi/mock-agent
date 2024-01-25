package io.github.lancelothuxi.mock.agent.mock;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.express.Expression;
import io.github.lancelothuxi.mock.agent.express.ExpressionFactory;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.util.CollectionUtils;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午5:19
 */
public abstract class CommonMockService {

    /**
     *
     */
    public Object doMock(String interfaceName, String methodName, String group, String version, Callable supercall,
                         Object[] args, Type genericReturnType) throws Exception {

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

        MockData mockData = getMockData(mockConfig,mockDataList, args);
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


    protected MockData getMockData(MockConfig mockConfig, List<MockData> mockDataList, Object[] args) {

        Expression expression = ExpressionFactory.getExpression(mockConfig.getExpressionType(),args);

        for (MockData mockData : mockDataList) {
            try {

                if(StringUtils.isEmpty(mockData.getExpression())){
                    return mockData;
                }

                Object jsonPathValue = expression.getValue(args, mockData.getExpression());
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
