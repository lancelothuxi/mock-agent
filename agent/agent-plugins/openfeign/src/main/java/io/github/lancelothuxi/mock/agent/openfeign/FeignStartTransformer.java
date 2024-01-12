package io.github.lancelothuxi.mock.agent.openfeign;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Transformer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class FeignStartTransformer implements Transformer {
    @Override
    public ElementMatcher<TypeDescription> classMatcher() {
        return ElementMatchers.<TypeDescription>named("com.ysepay.common.http.openfeign.FeignClientsRegistrar");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.<MethodDescription>named("registerFeignClient");
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return null;
    }

}
