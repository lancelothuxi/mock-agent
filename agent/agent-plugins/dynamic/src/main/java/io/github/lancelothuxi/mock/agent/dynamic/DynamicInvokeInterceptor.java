package io.github.lancelothuxi.mock.agent.dynamic;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.CommonInterceptor;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.mock.CommonMockService;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author lancelot
 */
public class DynamicInvokeInterceptor extends CommonMockService implements Interceptor {

    @Override
    public Object intercept(Method realMethod, Object[] args, Object self, Callable supercall) throws Exception {

        String methodName = realMethod.getName();
        String interfaceName = realMethod.getDeclaringClass().getName();
        final String argsString = JSON.toJSONString(args);

        Object result = super.doMock(interfaceName, methodName, "", "", supercall, argsString,
                realMethod.getGenericReturnType());

        return result;
    }
}
