package io.github.lancelothuxi.mock.agent.core;

import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * agent to intercept dubbo interfaces
 * @author lancelot
 */
public class MockAgent {

    private static final Logger logger = LoggerFactory.getLogger(MockAgent.class);

    public static void premain(String arguments, Instrumentation instrumentation) {

        logger.info("mock agent starting...");

        GlobalConfig.init();

        final AgentBuilder.Default agentBuilder = new AgentBuilder.Default();
        agentBuilder.ignore(ElementMatchers.<TypeDescription>nameStartsWith("java.")
                .or(ElementMatchers.<TypeDescription>nameStartsWith("net.bytebuddy."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("org.slf4j."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("sun."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("javassist"))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("com.taobao.")).or(ElementMatchers.isSynthetic()));

        PluginFinder pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());
        agentBuilder.type(pluginFinder.buildMatch()).transform(new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                                    ClassLoader classLoader, JavaModule module) {
                //todo match any class any method
                Transformer transformer = pluginFinder.find(typeDescription);
                return builder.method(transformer.methodMatcher()).intercept(MethodDelegation.withDefaultConfiguration()
                        .to(new CommonInterceptor(classLoader, transformer.interceptor().getName())));
            }
        }).installOn(instrumentation);
    }
}
