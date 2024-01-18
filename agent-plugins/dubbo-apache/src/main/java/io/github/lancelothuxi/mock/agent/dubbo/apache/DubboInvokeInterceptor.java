package io.github.lancelothuxi.mock.agent.dubbo.apache;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.mock.CommonMockService;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


/**
 * @author lancelot
 */
public class DubboInvokeInterceptor extends CommonMockService implements Interceptor {
    
    @Override
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments, @This Object self,
                            @SuperCall Callable supercall) throws Exception {

        // check if need mock
        MockClusterInvoker mockClusterInvoker = (MockClusterInvoker) self;
        Invocation invocation = ((Invocation) allArguments[0]);

        // skip CommonDubboMockService
        final String interfaceName = mockClusterInvoker.getInterface().getCanonicalName();

        String methodName = invocation.getMethodName();
        final String groupName = mockClusterInvoker.getUrl().getParameter(Constants.GROUP_KEY, "");
        final String version = mockClusterInvoker.getUrl().getParameter(Constants.VERSION_KEY, "");
        final String argsString = JSON.toJSONString(invocation.getArguments());
        final Method dubboMethod =
                mockClusterInvoker.getInterface().getMethod(methodName, invocation.getParameterTypes());

        Object o = super.doMock(interfaceName, methodName, groupName, version, supercall, argsString,
                dubboMethod.getGenericReturnType());
        return AsyncRpcResult.newDefaultAsyncResult(o,invocation);
    }

    @Override
    protected String getType() {
        return "dubbo";
    }
}
