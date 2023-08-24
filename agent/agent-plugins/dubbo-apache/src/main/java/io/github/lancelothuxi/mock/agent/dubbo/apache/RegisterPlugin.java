package io.github.lancelothuxi.mock.agent.dubbo.apache;

import static net.bytebuddy.matcher.ElementMatchers.named;

import io.github.lancelothuxi.mock.agent.core.Plugin;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 上午11:44
 */
public class RegisterPlugin implements Plugin {

    @Override
    public ElementMatcher classMatcher() {
        return named("com.alibaba.dubbo.config.spring.ReferenceBean");
    }

    @Override
    public ElementMatcher methodMatcher() {
        return named("afterPropertiesSet");
    }

    @Override
    public Class interceptor() {
        return null;
    }
}
