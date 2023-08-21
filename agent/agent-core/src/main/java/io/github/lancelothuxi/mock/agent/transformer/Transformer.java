package io.github.lancelothuxi.mock.agent.transformer;

import io.github.lancelothuxi.mock.agent.core.CommonInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/18 下午3:31
 */
public class Transformer implements AgentBuilder.Transformer {

    private List<Enhance> enhances = new ArrayList<>();


    public Transformer() {


    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                            ClassLoader classLoader, JavaModule javaModule) {


        return builder.method(named("invokeBeanFactoryPostProcessors")).intercept(
                MethodDelegation.withDefaultConfiguration().to(

                        new CommonInterceptor(classLoader, "com.lancelot.mock.agent.SpringApplicationNameIntercerptor")));
//                                                DubboInvokeInterceptor.class));
    }
}
