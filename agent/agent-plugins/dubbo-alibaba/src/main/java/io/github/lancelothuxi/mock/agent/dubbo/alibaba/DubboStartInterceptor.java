package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import com.alibaba.dubbo.config.spring.ReferenceBean;

import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;

public class DubboStartInterceptor implements Interceptor {

    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) {
        try {

            ReferenceBean referenceBean = (ReferenceBean)self;
            final String interfaceName = referenceBean.getInterface();
            final String groupName = referenceBean.getGroup();
            final String version = referenceBean.getVersion();

            // skip CommonDubboMockService
            if (interfaceName.equals(CommonDubboMockService.class.getName())) {
                return supercall.call();
            }

            if (StringUtils.isEmpty(GlobalConfig.applicationName)) {
                final String nameFromProperty = System.getProperty("mock.agent.applicationName");
                GlobalConfig.applicationName = nameFromProperty;
            }

            for (Method dubboMethod : referenceBean.getInterfaceClass().getMethods()) {
                MockConfig mockConfig = new MockConfig();
                mockConfig.setInterfaceName(interfaceName);
                mockConfig.setMethodName(dubboMethod.getName());
                mockConfig.setGroupName(StringUtils.isEmpty(groupName) ? "" : groupName);
                mockConfig.setVersion(StringUtils.isEmpty(version) ? "" : version);
                mockConfig.setApplicationName(GlobalConfig.applicationName);
                mockConfig.setType("dubbo");

                LogUtil.log("获取到dubbo应用依赖的provider interfacename={} methodname={} groupName={} version={}",
                    interfaceName, dubboMethod.getName(), groupName, version);
                MockConfigRegistry.add4Register(mockConfig);
            }

        } catch (Throwable throwable) {
            LogUtil.log("intercept dubbo 启动类失败", throwable);
        }

        try {
            return supercall.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
