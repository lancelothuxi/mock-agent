package io.github.lancelothuxi.mock.agent.examples.dubbo.apache;

import io.github.lancelothuxi.mock.agent.examples.Example;

import java.io.File;
import java.net.URL;


public class Application {

    public static void main(String[] args) throws Exception {

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("mock.agent.config.mode","file");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);


        DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
        dubboApacheExampleService.sayHelloByDubbo();
    }
}