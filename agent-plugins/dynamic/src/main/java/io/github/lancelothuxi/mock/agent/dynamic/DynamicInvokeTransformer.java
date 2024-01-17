package io.github.lancelothuxi.mock.agent.dynamic;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Transformer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

public class DynamicInvokeTransformer implements Transformer {

    private final String className;

    private final List<String> methodNames;

    public DynamicInvokeTransformer(String className, List<String> methodNames) {
        this.className = className;
        this.methodNames = methodNames;
    }

    @Override
    public ElementMatcher<TypeDescription> classMatcher() {
        return ElementMatchers.<TypeDescription>named(className);
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        ElementMatcher.Junction<MethodDescription> junction = ElementMatchers.<MethodDescription>named(methodNames.get(0));
        //必须有一个 后面做check
        if(methodNames.size()== 1){
            return junction;
        }

        for (int i = 1; i < methodNames.size(); i++) {
            junction= junction.or(ElementMatchers.<MethodDescription>named(methodNames.get(i)));
        }

        return junction;
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return DynamicInvokeInterceptor.class;
    }

}
