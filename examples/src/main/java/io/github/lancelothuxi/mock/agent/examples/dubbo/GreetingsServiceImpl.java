package io.github.lancelothuxi.mock.agent.examples.dubbo;

public class GreetingsServiceImpl implements GreetingsService {
    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}