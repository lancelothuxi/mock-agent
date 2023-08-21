package io.github.lancelothuxi.mock.agent.core;

import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;


/**
 * agent to intercept dubbo interfaces
 */
public class Agent {

    public static void premain(String arguments, Instrumentation instrumentation) {

        String mockAgentMandoryProperty = System.getProperty("mock.agent.mandatory");
        if (StringUtils.isNotEmpty(mockAgentMandoryProperty)) {
            Global.agentMandatory = "true".equals(mockAgentMandoryProperty);
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

        LogUtil.log("mock agent started...");
    }
}
