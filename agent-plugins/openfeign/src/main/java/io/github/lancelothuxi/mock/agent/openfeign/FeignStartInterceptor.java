package io.github.lancelothuxi.mock.agent.openfeign;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class FeignStartInterceptor implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) {
        try {

//            final String appName = GlobalConfig.applicationName;
//            Class<?> feignClient = Class.forName(className);
//
//            Method[] methods = feignClient.getMethods();
//            for (Method method1 : methods) {
//                MockConfig mockConfig = new MockConfig();
//                mockConfig.setInterfaceName(className);
//                mockConfig.setMethodName(method1.getName());
//                mockConfig.setGroupName("");
//                mockConfig.setVersion("");
//                mockConfig.setApplicationName(appName);
//                mockConfig.setType("feign");
//                MockConfigRegistry.add4Register(mockConfig);
//            }

            return supercall.call();
        } catch (Exception e) {
            try {
                return supercall.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
