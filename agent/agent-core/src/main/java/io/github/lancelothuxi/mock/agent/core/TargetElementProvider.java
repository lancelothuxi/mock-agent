package io.github.lancelothuxi.mock.agent.core;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/18 下午4:19
 */
public interface TargetElementProvider {

    /**
     * 监控方法描述
     * @return
     */
    ElementMatcher<MethodDescription> methodDescription();

    /**
     * 监控类型描述
     * @return
     */
    ElementMatcher<TypeDescription> typeDescription();

}
