package io.github.lancelothuxi.mock.agent.mock;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.util.CollectionUtils;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;

import static io.github.lancelothuxi.mock.agent.polling.Util.getMockData;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午5:19
 */
public abstract class CommonMockService {

    /**
     * @param interfaceName
     * @param methodName
     * @param group
     * @param version
     * @param supercall
     * @param argsString
     * @param genericReturnType
     * @return
     * @throws Exception
     */
    public Object doMock(String interfaceName, String methodName, String group, String version, Callable supercall,
                         String argsString, Type genericReturnType) throws Exception {

        MockConfig query = new MockConfig();
        query.setInterfaceName(interfaceName);
        query.setMethodName(methodName);
        query.setGroupName(group);
        query.setVersion(version);

        // try find config from local cache
        final MockConfig mockConfig = MockConfigRegistry.getMockConfig(query);

        if (mockConfig == null) {
            return supercall.call();
        }

        // 服务端mock
        if (mockConfig.mockFromServer()) {
            return mockFromServer(interfaceName, methodName, group, version, supercall, argsString, genericReturnType);
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

        data = compoundVariable.execute(argsString);

        Object mockValue = ParseUtil.parseMockValue(data, genericReturnType);

        return wrapReturnValue(mockValue);
    }

    /**
     * @param interfaceName
     * @param methodName
     * @param group
     * @param version
     * @return
     */
    public abstract Object mockFromServer(String interfaceName, String methodName, String group, String version,
                                          Callable supercall, String argsString, Type genericReturnType);

    /**
     * @param returnValue
     * @return
     */
    public abstract Object wrapReturnValue(Object returnValue);
}
