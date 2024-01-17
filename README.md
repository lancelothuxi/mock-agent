# Mock Agent

## 简介

Mock Agent 是一个无侵入式的 mock 工具，它使用 JavaAgent 技术，在运行时修改被 mock 的类的行为。
## 组件支持
- dubbo(alibaba)
- dubbo(apache)
- feign

## 任意类mock
可对任意类进行mock

## 功能

Mock Agent 支持以下功能：

* 方法拦截：可以拦截被 mock 的类的任何方法，并返回指定的返回值或抛出指定的异常。
* 参数修改：可以修改被 mock 的类的方法的参数。
* 方法重写：可以重写被 mock 的类的方法的行为。

## 使用

要使用 Mock Agent，需要在运行时添加以下参数：
```
-javaagent:/path/to/mock-agent.jar
```
其中，`/path/to/mock-agent.jar` 是 Mock Agent 的 jar 包路径。

## 配置


## 示例

以下是一个使用 Mock Agent 的示例：
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

注意事项
Mock Agent 可能会影响被 mock 的类的性能。


许可
Mock Agent 采用 MIT 许可。

