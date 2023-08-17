package io.github.lancelothuxi.mock.agent;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface Interceptor {

    public Object intercept(Method method, Object[] allArguments,
                            Object self, Callable supercall) throws Exception;
}
