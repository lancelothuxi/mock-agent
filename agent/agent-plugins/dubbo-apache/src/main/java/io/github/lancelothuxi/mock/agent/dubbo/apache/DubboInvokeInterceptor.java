package io.github.lancelothuxi.mock.agent.dubbo.apache;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.polling.Util;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import io.github.lancelothuxi.mock.api.MockRequest;
import io.github.lancelothuxi.mock.api.MockResponse;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;


public class DubboInvokeInterceptor implements Interceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        //移除掉序列化反序列化字段不匹配抛出异常 特性
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private volatile CommonDubboMockService commonDubboMockService;

    @Override
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments,
                            @This Object self, @SuperCall Callable supercall) throws Exception {

        //check if need mock
        MockClusterInvoker mockClusterInvoker = (MockClusterInvoker) self;
        Invocation invocation = ((Invocation) allArguments[0]);
        final String interfaceName = mockClusterInvoker.getInterface().getCanonicalName();

        try {
            String methodName = invocation.getMethodName();

            //CommonDubboMockService 不拦截
            if (interfaceName.equals(CommonDubboMockService.class.getName())) {
                return supercall.call();
            }
            final String groupName = mockClusterInvoker.getUrl().getParameter(CommonConstants.GROUP_KEY, "");
            final String version = mockClusterInvoker.getUrl().getParameter(CommonConstants.VERSION_KEY, "");

            LogUtil.log("entering DubboInterceptor by java agent interfaceName={} methodName={}", interfaceName, methodName);

            MockConfig query = new MockConfig();
            query.setInterfaceName(interfaceName);
            query.setMethodName(methodName);
            query.setGroupName(groupName);
            query.setVersion(version);

            //try find config from local cache
            final MockConfig mockConfig = MockConfigRegistry.getMockConfig(query);

            if (mockConfig == null) {
                return supercall.call();
            }


            final Method dubboMethod = mockClusterInvoker.getInterface().getMethod(methodName, invocation.getParameterTypes());
            if (dubboMethod == null) {
                LogUtil.log("dubboMethod is null by java agent for interfaceName={} methodName={}", interfaceName, methodName);
                throw new RuntimeException("mock agent 获取数据为空或者异常");
            }

            final String argsString = JSON.toJSONString(invocation.getArguments());

            if (mockConfig.mockFromServer()) {
                if (commonDubboMockService == null) {
                    synchronized (DubboInvokeInterceptor.class) {
                        RegistryConfig registryConfig = new RegistryConfig(GlobalConfig.zkAddress);
                        ReferenceConfig<CommonDubboMockService> referenceConfig = new ReferenceConfig<CommonDubboMockService>();
                        referenceConfig.setInterface(CommonDubboMockService.class);
                        referenceConfig.setRegistry(registryConfig);
                        referenceConfig.setProtocol("dubbo");
                        referenceConfig.setGeneric(false);
                        commonDubboMockService = referenceConfig.get();
                    }
                }

                MockRequest mockRequest = new MockRequest();
                mockRequest.setInterfaceName(interfaceName);
                mockRequest.setMethodName(methodName);
                mockRequest.setGroupName(groupName);
                mockRequest.setVersion(version);
                mockRequest.setArgs(argsString);
                mockRequest.setAppName(mockConfig.getApplicationName());

                LogUtil.log("mock-agent call mock request interfaceName={} methodName={} args={}", interfaceName, methodName, argsString);
                MockResponse mockResponse = commonDubboMockService.doMockRequest(mockRequest);

                if (mockResponse == null) {
                    LogUtil.log("mock-agent call mock response is null interfaceName={} methodName={} response={}", interfaceName, methodName, mockResponse.getData());
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }

                if (!mockResponse.success()) {
                    LogUtil.log("mock-agent call mock response is not success interfaceName={} methodName={} response={}", interfaceName, methodName, mockResponse.getData());
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }


                LogUtil.log("mock-agent call mock response is valid interfaceName={} methodName={} response={}", interfaceName, methodName, mockResponse.getData());


                String data = mockResponse.getData();
                if (data == null || data.length() == 0) {
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }

                JavaType javaType = OBJECT_MAPPER.constructType(dubboMethod.getGenericReturnType());
                Object result = ParseUtil.parseMockValue(data, javaType);
                return AsyncRpcResult.newDefaultAsyncResult(result, invocation);
            }

            final List<MockData> mockDataList = mockConfig.getMockDataList();
            if (CollectionUtils.isEmpty(mockDataList)) {
                throw new RuntimeException("mock agent 获取数据为空或者异常");
            }

            MockData mockData = Util.getMockData(mockDataList, argsString);
            if (mockData == null) {
                throw new RuntimeException("mock agent 获取数据为空或者异常");
            }

            if (mockData.getTimeout() != null && mockData.getTimeout() > 0) {
                Thread.sleep(mockData.getTimeout());
            }

            JavaType javaType = OBJECT_MAPPER.constructType(dubboMethod.getGenericReturnType());

            String data = mockData.getData();

            CompoundVariable compoundVariable = FunctionCache.get(data);
            if (compoundVariable == null) {
                compoundVariable = new CompoundVariable();
                compoundVariable.setParameters(data);
                FunctionCache.putIfAbsent(data, compoundVariable);
            }

            data = compoundVariable.execute(argsString);
            final Object result = ParseUtil.parseMockValue(data, javaType);
            return AsyncRpcResult.newDefaultAsyncResult(result, invocation);

        } catch (Exception e) {

            if (GlobalConfig.degrade) {
                return supercall.call();
            } else {
                throw e;
            }
        }
    }

}
