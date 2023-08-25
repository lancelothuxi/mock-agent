package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author lancelot
 */
public class DubboStartInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(DubboStartInterceptor.class);

    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) {
        try {

            ReferenceBean referenceBean = (ReferenceBean) self;
            final String interfaceName = referenceBean.getInterface();
            final String groupName = referenceBean.getGroup();
            final String version = referenceBean.getVersion();

            // skip CommonDubboMockService
            if (interfaceName.equals(CommonDubboMockService.class.getName())) {
                return supercall.call();
            }

            for (Method dubboMethod : referenceBean.getInterfaceClass().getMethods()) {
                MockConfig mockConfig = new MockConfig();
                mockConfig.setInterfaceName(interfaceName);
                mockConfig.setMethodName(dubboMethod.getName());
                mockConfig.setGroupName(StringUtils.isEmpty(groupName) ? "" : groupName);
                mockConfig.setVersion(StringUtils.isEmpty(version) ? "" : version);
                mockConfig.setApplicationName(GlobalConfig.applicationName);
                mockConfig.setType("dubbo");

                logger.info("获取到dubbo应用依赖的provider interfacename={} methodname={} groupName={} version={}",
                        interfaceName, dubboMethod.getName(), groupName, version);
                MockConfigRegistry.add4Register(mockConfig);
            }

        } catch (Throwable throwable) {
            logger.error("intercept dubbo 启动类失败", throwable);
        }

        try {
            return supercall.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
