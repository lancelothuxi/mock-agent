package io.github.lancelothuxi.mock.agent.core;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/18 下午4:20
 */
public interface Plugin {
    /**
     * classMatcher
     *
     * @return
     */
    ElementMatcher<TypeDescription>  classMatcher();

    /**
     * methodMatcher
     *
     * @return
     */
    ElementMatcher<? super MethodDescription> methodMatcher();

    /**
     * 织入类
     *
     * @return
     */
    Class<? extends Interceptor> interceptor();
}
