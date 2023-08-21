package io.github.lancelothuxi.mock.agent.core;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface Interceptor {

    /**
     *
     * @param method
     * @param allArguments
     * @param self
     * @param supercall
     * @return
     * @throws Exception
     */
    public Object intercept(Method method, Object[] allArguments,
                            Object self, Callable supercall) throws Exception;
}
