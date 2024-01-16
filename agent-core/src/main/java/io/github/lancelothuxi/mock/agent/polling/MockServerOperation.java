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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.lancelothuxi.mock.agent.config.Constant.MOCK_CONFIG_REGISTER_URL;
import static io.github.lancelothuxi.mock.agent.config.Constant.QUERY_MOCK_CONFIG_LIST_URL;

public class MockServerOperation {

    private static final Timer timer = new Timer();
    /**
     * dubbo
     */
    public static void sync() {
        final QueryMockConfigsRequest queryMockConfigsRequest = register(
                MockConfigRegistry.registryValues());

        syncMockConfigsFromServer(queryMockConfigsRequest);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                syncMockConfigsFromServer(queryMockConfigsRequest);
            }
        }, 0, 5000L);
    }



    private static void syncMockConfigsFromServer(QueryMockConfigsRequest queryMockConfigsRequest) {
        final String result = HttpUtil.sendPostRequest(QUERY_MOCK_CONFIG_LIST_URL,queryMockConfigsRequest, 3000);
        String md5Hex = DigestUtils
                .getMD5(result).toUpperCase();
        //数据有变化
        if (!md5Hex.equals(MockConfigRegistry.lastMd5)) {
            MockConfigRegistry.lastMd5 = md5Hex;
            final MockConfigListResponse mockConfigListResponse = JSON.parseObject(result, MockConfigListResponse.class);
            MockConfigRegistry.sync(mockConfigListResponse.getMockConfigs());
        }
    }


    private static QueryMockConfigsRequest register(List<MockConfig> mockConfigs) {
        QueryMockConfigsRequest queryMockConfigsRequest = new QueryMockConfigsRequest();
        queryMockConfigsRequest.setAppName(GlobalConfig.applicationName);
        queryMockConfigsRequest.setMockConfigList(mockConfigs);

        try {
            //注册
            HttpUtil.sendPostRequest(MOCK_CONFIG_REGISTER_URL, queryMockConfigsRequest,3000);
        }catch (Exception ex){
            if(GlobalConfig.degrade){
                throw new RuntimeException("注册请求失败，mock.agent.mandatory 设置为true, 程序将退出");
            }
        }
        return queryMockConfigsRequest;
    }
}
