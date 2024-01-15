package io.github.lancelothuxi.mock.agent.openfeign;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Transformer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class FeignInvokeTransformer implements Transformer {
    @Override
    public ElementMatcher<TypeDescription> classMatcher() {
        return ElementMatchers.<TypeDescription>named("feign.ReflectiveFeign$FeignInvocationHandler");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.<MethodDescription>named("invoke");
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return FeignInvokeInterceptor.class;
    }
}
