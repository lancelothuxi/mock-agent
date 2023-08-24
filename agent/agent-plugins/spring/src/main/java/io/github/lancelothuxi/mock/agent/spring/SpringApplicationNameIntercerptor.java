package io.github.lancelothuxi.mock.agent.spring;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.springframework.context.support.AbstractApplicationContext;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.core.Interceptor;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/13 下午8:44
 */
public class SpringApplicationNameIntercerptor implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) throws Exception {
        String applicationName =
            ((AbstractApplicationContext)self).getEnvironment().getProperty("spring.application.name");
        GlobalConfig.applicationName = applicationName;
        return supercall.call();
    }
}
