package io.github.lancelothuxi.mock.agent.examples;

import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.MockAgent;

public class Example {
    public static void main(String[] args) {

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("mock.agent.config.mode","file");
        System.setProperty("mock.agent.config.mode","file");

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"-D");

        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();

        // 调用被 mock 的方法
        int result = service.add(1, 2);

    }
}
