package io.github.lancelothuxi.mock.agent.examples;

import com.alibaba.fastjson.JSON;
import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.core.MockAgent;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class Example {
    public static void main(String[] args) throws Exception{

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("mock.agent.config.mode","file");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"");

        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();

        // 调用被 mock 的方法
        int result = service.add(1, 2);

        System.out.println("result = " + result);


    }
}
