package io.github.lancelothuxi.mock.agent.dynamic;

import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.core.Plugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

public class DynamicInvokePlugin implements Plugin {

    private final String className;

    private final List<String> methodNames;

    public DynamicInvokePlugin(String className, List<String> methodNames) {
        this.className = className;
        this.methodNames = methodNames;
    }

    @Override
    public ElementMatcher<TypeDescription> classMatcher() {
        return null;
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return null;
    }

    @Override
    public Class<? extends Interceptor> interceptor() {
        return null;
    }
}
