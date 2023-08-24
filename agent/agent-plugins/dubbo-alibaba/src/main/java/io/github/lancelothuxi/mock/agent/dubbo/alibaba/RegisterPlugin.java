package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

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
        return named("com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker");
    }

    @Override
    public ElementMatcher methodMatcher() {
        return named("invoke");
    }

    @Override
    public Class interceptor() {
        return DubboStartInterceptor.class;
    }
}
