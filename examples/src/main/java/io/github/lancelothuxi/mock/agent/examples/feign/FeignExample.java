package io.github.lancelothuxi.mock.agent.examples.feign;

import com.ea.agentloader.AgentLoader;
import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.lancelothuxi.mock.agent.core.MockAgent;
import io.github.lancelothuxi.mock.agent.examples.Example;

import java.io.File;
import java.net.URL;

public class FeignExample {

    public static void main(String[] args) throws Exception{

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("MOCK_SERVER_URL","http://localhost:8080");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"");

        HelloFeignService feignService = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .contract(new Contract.Default())
                .target(HelloFeignService.class, "http://localhost");

        String result = feignService.hello();

        //输出 result =  this is mock data for feign
        System.out.println("result = " + result);
    }
}
