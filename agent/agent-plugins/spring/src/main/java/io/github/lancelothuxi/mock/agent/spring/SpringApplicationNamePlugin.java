package io.github.lancelothuxi.mock.agent.spring;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Plugin;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午4:09
 */
public class SpringApplicationNamePlugin implements Plugin {
    @Override
    public ElementMatcher classMatcher() {
        return named("org.springframework.context.support.AbstractApplicationContext");
    }

    @Override
    public ElementMatcher methodMatcher() {
        return named("invokeBeanFactoryPostProcessors");
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return SpringApplicationNameIntercerptor.class;
    }
}
