package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.polling.MockServerOperation;
import io.github.lancelothuxi.mock.agent.util.StringUtils;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


public class Dubbo26StartInterceptor implements Interceptor {

    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) throws Exception {
        try {

            ReferenceBean referenceBean = (ReferenceBean) self;
            final String interfaceName = referenceBean.getInterface();
            final String groupName = referenceBean.getGroup();
            final String version = referenceBean.getVersion();
            if (interfaceName.equals(CommonDubboMockService.class.getName())) {
                return supercall.call();
            }

            if (StringUtils.isEmpty(Global.applicationName)) {
                final String nameFromProperty = System.getProperty("mock.agent.applicationName");
                Global.applicationName = nameFromProperty;
            }

            for (Method dubboMethod : referenceBean.getInterfaceClass().getMethods()) {
                MockConfig mockConfig = new MockConfig();
                mockConfig.setInterfaceName(interfaceName);
                mockConfig.setMethodName(dubboMethod.getName());
                mockConfig.setGroupName(StringUtils.isEmpty(groupName) ? "" : groupName);
                mockConfig.setVersion(StringUtils.isEmpty(version) ? "" : version);
                mockConfig.setAppliactionName(Global.applicationName);

                LogUtil.log("获取到dubbo应用依赖的provider interfacename={} methodname={} groupName={} version={}", interfaceName, dubboMethod.getName(), groupName, version);
                MockConfigRegistry.add4Register(mockConfig);
            }

            MockServerOperation.registerAndGet4Dubbo();
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
