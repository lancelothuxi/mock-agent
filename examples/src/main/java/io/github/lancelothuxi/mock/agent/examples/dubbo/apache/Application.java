package io.github.lancelothuxi.mock.agent.examples.dubbo.apache;

import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.MockAgent;
import io.github.lancelothuxi.mock.agent.examples.Example;

import java.io.File;
import java.net.URL;


public class Application {

    public static void main(String[] args) throws Exception {

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("MOCK_SERVER_URL","http://localhost:8080");

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"");

        while (true){

            try {
                DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
                String sayHelloByDubbo = dubboApacheExampleService.sayHelloByDubbo();
                //输出： sayHelloByDubbo =  this is mock data
                System.out.println("sayHelloByDubbo = " + sayHelloByDubbo);
            }catch (Exception ex){
                //
            }finally {
                Thread.sleep(1000);
            }
        }
    }
}