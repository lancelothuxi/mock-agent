# Mock Agent

## 简介

Mock Agent 是一个无侵入式的 mock 工具，它使用 JavaAgent 技术，在运行时修改被 mock 的类的行为。

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

Mock Agent 的配置文件位于 `conf/mock.properties`。该文件包含以下配置项：

* `mock.classes`: 需要 mock 的类的列表。
* `mock.methods`: 需要 mock 的类的方法的列表。
* `mock.return`: 方法的返回值。
* `mock.exception`: 方法抛出的异常。

## 示例

以下是一个使用 Mock Agent 的示例：

```java
public class Example {

    public static void main(String[] args) {
        // 添加 Mock Agent 参数
        System.setProperty("javaagent", "/path/to/mock-agent.jar");

        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();

        // 调用被 mock 的方法
        int result = service.add(1, 2);

        // 验证结果
        assertEquals(3, result);
    }
}

class ExampleService {

    public int add(int a, int b) {
        return a + b;
    }
}
```

在上述示例中，我们需要 mock `ExampleService` 类的 `add()` 方法。在 `mock.properties` 文件中，我们添加如下配置：


mock.classes = ExampleService
mock.methods = add
mock.return = 3

运行上述示例，我们将得到以下输出：

```
[INFO] Initializing Mock Agent...
[INFO] Mocking class ExampleService
[INFO] Mocking method add
[INFO] Mock Agent started
[INFO] ExampleService.add(1, 2) = 3
```
注意事项
Mock Agent 只能 mock 已加载的类。
Mock Agent 可能会影响被 mock 的类的性能。
贡献
欢迎您对 Mock Agent 进行贡献。您可以提交 issue 或 pull request。


许可
Mock Agent 采用 MIT 许可。

