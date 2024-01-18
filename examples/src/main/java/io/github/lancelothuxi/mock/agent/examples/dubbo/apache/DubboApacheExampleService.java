package io.github.lancelothuxi.mock.agent.examples.dubbo.apache;

import io.github.lancelothuxi.mock.agent.examples.dubbo.GreetingsService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class DubboApacheExampleService {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public String sayHelloByDubbo(){
        ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingsService.class);
        GreetingsService service = reference.get();
        String message = service.sayHi("dubbo");
        return message;
    }
}
