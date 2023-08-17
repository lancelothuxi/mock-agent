package io.github.lancelothuxi.mock.agent.dubbo26;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import com.alibaba.dubbo.rpc.support.MockInvoker;
import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.Interceptor;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import io.github.lancelothuxi.mock.api.MockRequest;
import io.github.lancelothuxi.mock.api.MockResponse;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static io.github.lancelothuxi.mock.agent.polling.Util.getMockData;


public class Dubbo26InvokeInterceptor implements Interceptor {

    private volatile CommonDubboMockService commonDubboMockService;

    @Override
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments,
                            @This Object self, @SuperCall Callable supercall) throws Exception {

        //check if need mock
        MockClusterInvoker mockClusterInvoker = (MockClusterInvoker) self;
        Invocation invocation = ((Invocation) allArguments[0]);

        final String interfaceName = mockClusterInvoker.getInterface().getCanonicalName();
        String methodName = invocation.getMethodName();
        if (interfaceName.equals(CommonDubboMockService.class.getName())) {
            return supercall.call();
        }

        try {

            final String groupName = mockClusterInvoker.getUrl().getParameter(Constants.GROUP_KEY, "");
            final String version = mockClusterInvoker.getUrl().getParameter(Constants.VERSION_KEY, "");

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
                    synchronized (Dubbo26InvokeInterceptor.class) {
                        RegistryConfig registryConfig = new RegistryConfig(Global.zkAddress);
                        ReferenceConfig<CommonDubboMockService> referenceConfig = new ReferenceConfig<CommonDubboMockService>();
                        referenceConfig.setInterface(CommonDubboMockService.class);
                        referenceConfig.setRegistry(registryConfig);
                        referenceConfig.setProtocol("dubbo");
                        referenceConfig.setGeneric(false);
                        ApplicationConfig applicationConfig=new ApplicationConfig();
                        applicationConfig.setName(StringUtils.isEmpty(Global.applicationName)?"unknown":Global.applicationName);
                        referenceConfig.setApplication(applicationConfig);
                        commonDubboMockService = referenceConfig.get();
                    }
                }

                MockRequest mockRequest = new MockRequest();
                mockRequest.setInterfaceName(interfaceName);
                mockRequest.setMethodName(methodName);
                mockRequest.setGroupName(groupName);
                mockRequest.setVersion(version);
                mockRequest.setArgs(argsString);
                mockRequest.setAppName(mockConfig.getAppliactionName());

                LogUtil.log("mock-agent call mock request interfaceName={} methodName={} args={}", interfaceName, methodName,argsString);
                MockResponse mockResponse = commonDubboMockService.doMockRequest(mockRequest);

                if (mockResponse == null ) {
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


                final Class<?> returnType = dubboMethod.getReturnType();
                Object mockValue = MockInvoker.parseMockValue(data, new Class[]{returnType});
//                JavaType javaType = OBJECT_MAPPER.constructType(dubboMethod.getGenericReturnType());
//                Object result = ParseUtil.parseMockValue(data, javaType);
                return new RpcResult(mockValue);
            }

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
            final Class<?> returnType = dubboMethod.getReturnType();
            Object mockValue = MockInvoker.parseMockValue(data, new Class[]{returnType});
            return new RpcResult(mockValue);
        } catch (Throwable e) {
            LogUtil.log("mock-agent call dubbo rest mock has error Global.agentMandatory={}",Global.agentMandatory, e);
            if(Global.agentMandatory){
                throw e;
            }else {
                return supercall.call();
            }
        }
    }

}
