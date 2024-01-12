package io.github.lancelothuxi.mock.agent.dubbo.apache;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Transformer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 上午11:44
 */
public class InvokerTransformer implements Transformer {

    @Override
    public ElementMatcher<TypeDescription> classMatcher() {
        return ElementMatchers.<TypeDescription>named("org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return named("invoke");
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return DubboInvokeInterceptor.class;
    }
}
