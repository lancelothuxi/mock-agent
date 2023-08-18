package io.github.lancelothuxi.mock.agent.feign;

import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.Interceptor;
import feign.Client;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author liulei1971
 * @date 2023/5/30 20:39
 */
public class FeignFactoryBeanInterceptor implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) {
        try {

            BeanFactory beanFactories = (BeanFactory) allArguments[0];
            Client client = beanFactories.getBean(Client.class);
            if (Global.feignClient == null) {
                Global.feignClient = client;
            }
            return supercall.call();
        } catch (Exception e) {
            try {
                return supercall.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
