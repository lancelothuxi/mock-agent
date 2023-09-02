package io.github.lancelothuxi.mock.agent.polling;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.MockAgent;
import io.github.lancelothuxi.mock.agent.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MockServerOperation {
    private static Logger logger = LoggerFactory.getLogger(MockAgent.class);

    private static String registerUrl = "/mock/dubbo/config/register";
    private static String queryAllURL = "/mock/dubbo/config/getAllByType";

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private static volatile boolean dubboTaskStarted = false;

    /**
     * dubbo
     */
    public static void register() {

        final Request request = register( MockConfigRegistry.registryValues());
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
        final String result = HttpUtil.sendPostRequest(queryAllURL,request, 3000);
        String md5Hex = DigestUtils.getMD5(result).toUpperCase();

        // 数据有变化
        if (!md5Hex.equals(MockConfigRegistry.lastMd5)) {
            MockConfigRegistry.lastMd5 = md5Hex;
            final Response response = JSON.parseObject(result, Response.class);
            MockConfigRegistry.sync(response.getMockConfigs());
        }
    }

    private static Request register(List<MockConfig> mockConfigs) {
        Request request = new Request();
        request.setAppName(GlobalConfig.applicationName);
        request.setMockConfigList(mockConfigs);

        try {
            // 注册
            HttpUtil.sendPostRequest(registerUrl,request, 3000);
        } catch (Exception ex) {
            logger.error("注册失败",ex);
        }
        return request;
    }
}
