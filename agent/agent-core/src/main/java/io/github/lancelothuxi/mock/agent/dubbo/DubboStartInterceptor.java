package io.github.lancelothuxi.mock.agent.dubbo;

import com.alibaba.dubbo.common.Constants;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockServerOperation;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.alibaba.dubbo.common.Constants.CONSUMER_SIDE;
import static io.github.lancelothuxi.mock.agent.util.Util.mapGetOrDefault;

public class DubboStartInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method, @Argument(0) Map<String, String> map,
                                   @This Object self, @SuperCall Callable supercall) throws Exception {
        try {
            final String s = map.get(Constants.SIDE_KEY);

            if (CONSUMER_SIDE.equals(s)) {

                final String interfaceName = mapGetOrDefault(map, Constants.INTERFACE_KEY, "");
                if (interfaceName.equals(CommonDubboMockService.class.getName())) {
                    return supercall.call();
                }


                final String appName = Global.applicationName;

                final String[] methodNames = mapGetOrDefault(map, Constants.METHODS_KEY, "").split(",");
                final String groupName = mapGetOrDefault(map, Constants.GROUP_KEY, "");
                final String version = mapGetOrDefault(map, Constants.VERSION_KEY, "");

                for (String methodName : methodNames) {

                    MockConfig mockConfig = new MockConfig();
                    mockConfig.setInterfaceName(interfaceName);
                    mockConfig.setMethodName(methodName);
                    mockConfig.setGroupName(groupName);
                    mockConfig.setVersion(version);
                    mockConfig.setAppliactionName(appName);

                    LogUtil.log("获取到dubbo应用依赖的provider interfacename={} methodname={} groupName={} version={}", interfaceName, methodName, groupName, version);
                    MockConfigRegistry.add4Register(mockConfig);
                }

                MockServerOperation.registerAndGet4Dubbo();
            }
        } catch (Throwable throwable) {
            LogUtil.log("intercept dubbo 启动类失败", throwable);
        }

        return supercall.call();
    }
}