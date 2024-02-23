package io.github.lancelothuxi.mock.agent.examples.dubbo.apache;

import io.github.lancelothuxi.mock.agent.examples.dubbo.GreetingsService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class DubboApacheExampleService {
    public String sayHelloByDubbo(){
        ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setCheck(false);
        reference.setRegistry(new RegistryConfig("224.5.6.7:1234","multicast"));
        reference.setInterface(GreetingsService.class);
        GreetingsService service = reference.get();
        String message = service.sayHi("dubbo");
        return message;
    }
}
