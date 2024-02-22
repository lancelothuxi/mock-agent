package io.github.lancelothuxi.mock.agent.openfeign;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.mock.CommonMockService;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author liulei1971
 * @date 2023/5/30 20:39
 */
public class FeignInvokeInterceptor extends CommonMockService implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) throws Exception {
        Method realMethod = (Method) allArguments[1];

        String methodName = realMethod.getName();
        if ("equals".equals(methodName) || "hashCode".equals(methodName) || "toString".equals(methodName)) {
            return supercall.call();
        }

        String interfaceName = realMethod.getDeclaringClass().getName();
        MockConfig query = new MockConfig();
        query.setInterfaceName(interfaceName);
        query.setEnabled(1);
        query.setMethodName(methodName);
        query.setGroupName("");
        query.setVersion("");
        query.setType("feign");
        query.setApplicationName(GlobalConfig.applicationName);

        return super.doMock(interfaceName, methodName, "", "", supercall, allArguments,
                method.getGenericReturnType());
    }

    @Override
    protected String getType() {
        return "feign";
    }
}
