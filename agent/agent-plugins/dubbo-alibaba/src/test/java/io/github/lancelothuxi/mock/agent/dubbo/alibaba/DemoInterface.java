package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import io.github.lancelothuxi.mock.agent.util.DemoGeneric;
import io.github.lancelothuxi.mock.agent.util.DemoRequest;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/26 下午4:02
 */
public interface DemoInterface<T> {

  public DemoGeneric<T> hello(DemoRequest demoRequest);
}
