package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.mock.CommonMockService;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import io.github.lancelothuxi.mock.api.MockRequest;
import io.github.lancelothuxi.mock.api.MockResponse;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import static io.github.lancelothuxi.mock.agent.util.ConfigUtil.getPropertyFromEnvOrSystemProperty;

/**
 * @author lancelot
 */
public class DubboInvokeInterceptor extends CommonMockService implements Interceptor {
    
    private static Logger logger = LoggerFactory.getLogger(DubboInvokeInterceptor.class);

    private volatile CommonDubboMockService commonDubboMockService;

    @Override
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments, @This Object self,
                            @SuperCall Callable supercall) throws Exception {

        // check if need mock
        MockClusterInvoker mockClusterInvoker = (MockClusterInvoker) self;
        Invocation invocation = ((Invocation) allArguments[0]);

        // skip CommonDubboMockService
        final String interfaceName = mockClusterInvoker.getInterface().getCanonicalName();
        if (interfaceName.equals(CommonDubboMockService.class.getName())) {
            return supercall.call();
        }

        String methodName = invocation.getMethodName();
        final String groupName = mockClusterInvoker.getUrl().getParameter(Constants.GROUP_KEY, "");
        final String version = mockClusterInvoker.getUrl().getParameter(Constants.VERSION_KEY, "");
        final String argsString = JSON.toJSONString(invocation.getArguments());
        final Method dubboMethod =
                mockClusterInvoker.getInterface().getMethod(methodName, invocation.getParameterTypes());

        return super.doMock(interfaceName, methodName, groupName, version, supercall, argsString,
                dubboMethod.getGenericReturnType());
    }

    /**
     * dubbo服务端mock 通过代码方式生成CommonDubboMockService接口的dubbo代理类
     * 通过dubbo协议请求 mock server端的 CommonDubboMockService
     *
     * @param interfaceName
     * @param methodName
     * @param group
     * @param version
     * @param supercall
     * @param argsString
     * @param genericReturnType
     * @return
     */
    @Override
    public Object mockFromServer(String interfaceName, String methodName, String group, String version,
                                 Callable supercall, String argsString, Type genericReturnType) {
        if (commonDubboMockService == null) {
            synchronized (DubboInvokeInterceptor.class) {

                String zkAddress = getPropertyFromEnvOrSystemProperty("MOCK_AGENT_ZK_ADDRESS");
                RegistryConfig registryConfig = new RegistryConfig(zkAddress);
                ReferenceConfig<CommonDubboMockService> referenceConfig = new ReferenceConfig<CommonDubboMockService>();
                referenceConfig.setInterface(CommonDubboMockService.class);
                referenceConfig.setRegistry(registryConfig);
                referenceConfig.setProtocol("dubbo");
                referenceConfig.setGeneric(false);
                ApplicationConfig applicationConfig = new ApplicationConfig();
                applicationConfig.setName(
                        StringUtils.isEmpty(GlobalConfig.applicationName) ? "unknown" : GlobalConfig.applicationName);
                referenceConfig.setApplication(applicationConfig);
                commonDubboMockService = referenceConfig.get();
            }
        }

        MockRequest mockRequest = new MockRequest();
        mockRequest.setInterfaceName(interfaceName);
        mockRequest.setMethodName(methodName);
        mockRequest.setGroupName(group);
        mockRequest.setVersion(version);
        mockRequest.setArgs(argsString);
        mockRequest.setAppName(GlobalConfig.applicationName);

        logger.info("mock-agent call mock request interfaceName={} methodName={} args={}", interfaceName, methodName,
                argsString);
        MockResponse mockResponse = commonDubboMockService.doMockRequest(mockRequest);

        if (mockResponse == null) {
            logger.info("mock-agent call mock response is null interfaceName={} methodName={} response={}",
                    interfaceName, methodName, mockResponse.getData());
            throw new RuntimeException("mock agent 获取数据为空或者异常");
        }

        if (!mockResponse.success()) {
            logger.info("mock-agent call mock response is not success interfaceName={} methodName={} response={}",
                    interfaceName, methodName, mockResponse.getData());
            throw new RuntimeException("mock agent 获取数据为空或者异常");
        }

        logger.info("mock-agent call mock response is valid interfaceName={} methodName={} response={}", interfaceName,
                methodName, mockResponse.getData());

        String data = mockResponse.getData();
        if (data == null || data.length() == 0) {
            throw new RuntimeException("mock agent 获取数据为空或者异常");
        }

        Object mockValue = ParseUtil.parseMockValue(data, genericReturnType);
        return mockValue;
    }

    @Override
    public Object wrapReturnValue(Object returnValue) {
        return new RpcResult(returnValue);
    }
}
