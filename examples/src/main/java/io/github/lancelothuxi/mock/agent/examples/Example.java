package io.github.lancelothuxi.mock.agent.examples;

import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.MockAgent;

public class Example {
    public static void main(String[] args) {

        AgentLoader.loadAgentClass(MockAgent.class.getName(), "Hello!");

        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();

        // 调用被 mock 的方法
        int result = service.add(1, 2);

    }
}
