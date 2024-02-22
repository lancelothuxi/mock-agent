package io.github.lancelothuxi.mock.agent.examples.dubbo.alibaba;

import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.MockAgent;
import io.github.lancelothuxi.mock.agent.examples.Example;

import java.io.File;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws Exception {

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("MOCK_SERVER_URL","http://localhost:8080");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"");

        DubboAlibabaExampleService dubboAlibabaExampleService =new DubboAlibabaExampleService();
        String sayHelloByDubbo = dubboAlibabaExampleService.sayHelloByDubbo();

        //输出： sayHelloByDubbo =  this is mock data
        System.out.println("sayHelloByDubbo = " + sayHelloByDubbo);
    }
}
