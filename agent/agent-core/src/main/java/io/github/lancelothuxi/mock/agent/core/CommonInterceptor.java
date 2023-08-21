package io.github.lancelothuxi.mock.agent.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class CommonInterceptor {

    private InterceptorClassLoader loader;

    private Interceptor delegate;

    public CommonInterceptor(ClassLoader targetClassLoader, String delegaterClassname) {
        this.loader = new InterceptorClassLoader(targetClassLoader);
        delegate = InterceptorInstanceLoader.load(delegaterClassname, loader);
    }


    public Class loadClass(String clazzName) {
        try {
            return loader.findClass(clazzName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @RuntimeType
    public Object intercept(@Origin Method method, @AllArguments Object[] allArguments,
                            @This Object self, @SuperCall Callable supercall) throws Exception {

        if (delegate == null) {
            try {
                return supercall.call();
            } catch (Exception e) {
                //ignore
            }
        }

        return delegate.intercept(method, allArguments, self, supercall);
    }
}
