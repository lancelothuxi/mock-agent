package io.github.lancelothuxi.mock.agent.core;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/18 下午4:20
 */
public interface Plugin {
    /**
     *  name
     * @return
     */
    String name();

    /**
     * 提供者
     * @return
     */
    TargetElementProvider[] elementMatchers();

    /**
     * 织入类
     * @return
     */
    Class<?> advice();

}
