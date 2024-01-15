package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import io.github.lancelothuxi.mock.agent.util.DemoRequest;
import io.github.lancelothuxi.mock.agent.util.Person;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/26 下午4:02
 */
public interface DemoInterface<T> {

  public Person hello(DemoRequest demoRequest);
}
