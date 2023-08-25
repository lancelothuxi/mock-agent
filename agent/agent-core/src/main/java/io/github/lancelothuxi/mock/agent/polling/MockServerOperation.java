package io.github.lancelothuxi.mock.agent.polling;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.util.DigestUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MockServerOperation {

    private static String register = "/mock/dubbo/config/register";
    private static String queryAll = "/mock/dubbo/config/getAllByType";

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private static volatile boolean dubboTaskStarted = false;

    /**
     * dubbo
     */
    public static void registerAndGet4Dubbo() {

        final Request request = register("dubbo", MockConfigRegistry.registryValues());
        syncDubboConfigsFromServer(request);
        if (dubboTaskStarted) {
            return;
        }

        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                syncDubboConfigsFromServer(request);
            }
        }, 5, TimeUnit.SECONDS);

        dubboTaskStarted = true;
    }

    private static void syncDubboConfigsFromServer(Request request) {
        final String result = HttpUtil.doPost(request, queryAll, 3000);
        String md5Hex = DigestUtils.getMD5(result).toUpperCase();

        // 数据有变化
        if (!md5Hex.equals(MockConfigRegistry.lastMd5)) {
            MockConfigRegistry.lastMd5 = md5Hex;
            final Response response = JSON.parseObject(result, Response.class);
            MockConfigRegistry.sync(response.getMockConfigs());
        }
    }

    private static Request register(String type, List<MockConfig> mockConfigs) {
        Request request = new Request();
        request.setType(type);
        request.setAppName(GlobalConfig.applicationName);
        request.setMockConfigList(mockConfigs);

        try {
            // 注册
            HttpUtil.doPost(request, register, 3000);
        } catch (Exception ex) {

        }
        return request;
    }
}
