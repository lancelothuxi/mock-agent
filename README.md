# Mock Agent

## 简介

Mock Agent 是一个无侵入式的 mock 工具，它使用 JavaAgent 技术，在运行时修改被 mock 的类的行为。
## 组件支持
- dubbo(alibaba)
- dubbo(apache)
- feign

## 任意类mock
可对任意类进行mock

## 使用

要使用 Mock Agent，需要在运行时添加以下参数：
```
-javaagent:/path/to/mock-agent.jar
```
其中，`/path/to/mock-agent.jar` 是 Mock Agent 的 jar 包路径。

或者通过添加依赖(JDK版本<=1.8)
``` xml
     <dependency>
            <groupId>com.ea.agentloader</groupId>
            <artifactId>ea-agent-loader</artifactId>
            <version>1.0.3</version>
     </dependency> 
``` 
程序入口出添加
``` java 
AgentLoader.loadAgentClass(MockAgent.class.getName(),"");
```
## 配置


## 示例

以下是一个使用 Mock Agent 对任意类mock的示例：
```json
[
  {
    "applicationName": "Example",
    "enabled": 1,
    "interfaceName": "io.github.lancelothuxi.mock.agent.examples.ExampleService",
    "methodName": "add",
    "mockDataList": [
      {
        "data": "5",
        "timeout": 0
      }
    ],
    "serverSideMock": 1,
    "type": "dynamic"
  }
]
```

```java
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

        //改变方法返回值，输出：result = 5
        System.out.println("result = " + result);
    }
}

class ExampleService {

    public int add(int a, int b) {
        return a + b;
    }
}
```

以下是一个使用 Mock Agent 对dubbo调用mock的示例：
```json
[
  {
    "applicationName": "Example",
    "interfaceName": "io.github.lancelothuxi.mock.agent.examples.dubbo.GreetingsService",
    "methodName": "sayHi",
    "mockDataList": [
      {
        "data": " this is mock data ",
        "timeout": 0
      }
    ],
    "type": "dubbo"
  }
]
```

```java
public class Application {

    public static void main(String[] args) throws Exception {

        System.setProperty("MOCK_APPLICATION_NAME","Example");
        System.setProperty("mock.agent.config.mode","file");

        URL resource = Example.class.getClassLoader().getResource("mockconfig.json");
        File file = new File(resource.toURI());

        String path = file.getPath();
        System.setProperty("mock.agent.config.file.path",path);

        AgentLoader.loadAgentClass(MockAgent.class.getName(),"");

        DubboApacheExampleService dubboApacheExampleService =new DubboApacheExampleService();
        String sayHelloByDubbo = dubboApacheExampleService.sayHelloByDubbo();

        //输出： sayHelloByDubbo =  this is mock data
        System.out.println("sayHelloByDubbo = " + sayHelloByDubbo);
    }
}

public interface GreetingsService {
    String sayHi(String name);
}
```

更多示例请参考  [examples](./examples)
注意事项
Mock Agent 可能会影响被 mock 的类的性能。


许可
Mock Agent 采用 MIT 许可。

