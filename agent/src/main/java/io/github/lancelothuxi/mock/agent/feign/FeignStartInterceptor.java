package io.github.lancelothuxi.mock.agent.feign;

import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.Interceptor;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockServerOperation;
import io.github.lancelothuxi.mock.agent.config.registry.FeignMockConfigRegistry;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author liulei1971
 * @date 2023/5/30 20:39
 */
public class FeignStartInterceptor implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) {
        try {

            final String appName = Global.applicationName;

            AnnotationMetadata metadata = (AnnotationMetadata) allArguments[1];
            String className = metadata.getClassName();
            Class<?> feignClient = Class.forName(className);

            Method[] methods = feignClient.getMethods();
            for (Method method1 : methods) {

                MockConfig dubboRestMockConfig = new MockConfig();
                dubboRestMockConfig.setInterfaceName(className);
                dubboRestMockConfig.setMethodName(method1.getName());
                dubboRestMockConfig.setGroupName("");
                dubboRestMockConfig.setVersion("");
                dubboRestMockConfig.setAppliactionName(appName);
                FeignMockConfigRegistry.add4Register(dubboRestMockConfig);
            }

            MockServerOperation.registerAndGet4Feign();

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
