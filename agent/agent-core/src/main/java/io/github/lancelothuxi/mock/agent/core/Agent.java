package io.github.lancelothuxi.mock.agent.core;

import io.github.lancelothuxi.mock.agent.CommonInterceptor;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.dubbo.DubboStartInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * agent to intercept dubbo interfaces
 * <p>
 */
public class Agent {

    static {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
    }

    public static void premain(String arguments, Instrumentation instrumentation) {

        String mockAgentMandoryProperty = System.getProperty("mock.agent.mandatory");
        if(StringUtils.isNotEmpty(mockAgentMandoryProperty)){
            Global.agentMandatory="true".equals(mockAgentMandoryProperty);
        }

        System.out.println("mock-agent starting...  agentMandatory set to " + Global.agentMandatory);

        final AgentBuilder.Default agentBuilder = new AgentBuilder.Default();
        agentBuilder.ignore(ElementMatchers.<TypeDescription>nameStartsWith("java.")
                .or(ElementMatchers.<TypeDescription>nameStartsWith("net.bytebuddy."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("org.slf4j."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("sun."))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("javassist"))
                .or(ElementMatchers.<TypeDescription>nameStartsWith("com.taobao."))
                .or(ElementMatchers.isSynthetic())
        );

        //获取应用唯一标识，这里取springboot的 application Name
        agentBuilder.type(named("org.springframework.context.support.AbstractApplicationContext"))
                .transform(
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {


                                return builder.method(named("invokeBeanFactoryPostProcessors")).intercept(
                                        MethodDelegation.withDefaultConfiguration().to(

                                                new CommonInterceptor(classLoader, "com.lancelot.mock.agent.SpringApplicationNameIntercerptor")));
//                                                DubboInvokeInterceptor.class));
                            }
                        }
                ).installOn(instrumentation);

        // dubbo27 拦截invoke方法
        agentBuilder.type(named("org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker"))
                .transform(
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {
                                String interceptorClassName="com.lancelot.mock.agent.dubbo.DubboInvokeInterceptor";

                                return builder.method(named("invoke")).intercept(
                                        MethodDelegation.withDefaultConfiguration().to(
                                                new CommonInterceptor(classLoader, interceptorClassName)));
                            }
                        }
                ).installOn(instrumentation);

        //注解方式l配置依赖的dubbo interface方式拦截
        agentBuilder.type(named("org.apache.dubbo.config.ReferenceConfig"))
                .transform(

                        //拦截器没有call 其他类加载器加载的类，无需做classloader转换
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {

                                return builder.method(named("createProxy")).intercept(
                                        MethodDelegation.to(DubboStartInterceptor.class));
                            }
                        }
                ).installOn(instrumentation);




        /**dubbo26相关**/

        // 匹配被拦截方法
        agentBuilder.type(named("com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker"))
                .transform(
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {
                                return builder.method(named("invoke")).intercept(
                                        MethodDelegation.withDefaultConfiguration().to(
                                                new CommonInterceptor(classLoader, "com.lancelot.mock.agent.dubbo26.Dubbo26InvokeInterceptor")));
                            }
                        }
                ).installOn(instrumentation);


        //注解方式l配置依赖的dubbo interface方式拦截
        agentBuilder.type(named("com.alibaba.dubbo.config.spring.ReferenceBean"))
                .transform(

                        //拦截器没有call 其他类加载器加载的类，无需做classloader转换
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {
                                return builder.method(named("afterPropertiesSet")).intercept(
                                        MethodDelegation.withDefaultConfiguration().to(
                                                new CommonInterceptor(classLoader, "com.lancelot.mock.agent.dubbo26.Dubbo26StartInterceptor")));
                            }
                        }
                ).installOn(instrumentation);



        //xml配置依赖的dubbo interface方式拦截
        // 匹配被拦截方法
        agentBuilder.type(named("com.alibaba.dubbo.config.spring.schema.DubboBeanDefinitionParser"))
                .transform(
                        new AgentBuilder.Transformer() {
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {

                                return builder.method(named("parse").and(ElementMatchers.takesArguments(2))).intercept(
                                        MethodDelegation.withDefaultConfiguration().to(
                                                new CommonInterceptor(classLoader, "com.lancelot.mock.agent.dubbo26.DubboXmlRefStartInterceptor")));
                            }
                        }
                ).installOn(instrumentation);



        LogUtil.log("mock agent started...");
    }
}
