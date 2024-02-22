package io.github.lancelothuxi.mock.agent.examples;

import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.MockAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

public class Example {
    public static void main(String[] args) throws Exception{

       final Logger logger = LoggerFactory.getLogger(MockAgent.class);
        logger.info("aaa");

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("mock.agent.config.mode","file");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"a=b");

        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();

        // 调用被 mock 的方法 包含 ognl 条件表达式
        int result1 = service.add(1, 2);

        //输出 result1 = 4
        System.out.println("result1 = " + result1);

        // 调用被 mock 的方法 不含包含 ognl 条件表达式
        int result2 = service.add(4, 5);

        //输出 result1 = 5
        System.out.println("result2 = " + result2);


    }
}
