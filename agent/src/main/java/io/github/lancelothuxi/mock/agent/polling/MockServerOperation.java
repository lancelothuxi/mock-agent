package io.github.lancelothuxi.mock.agent.polling;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.config.registry.DubboRestMockConfigRegistry;
import io.github.lancelothuxi.mock.agent.config.registry.FeignMockConfigRegistry;
import io.github.lancelothuxi.mock.agent.util.DigestUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MockServerOperation {

    private static final int LONG_POLLING_READ_TIMEOUT = 90 * 1000;
    private static String register = "/mock/dubbo/config/register";
    private static String queryAll = "/mock/dubbo/config/getAllByType";

    private static Timer timer = new Timer();

    //ugly code hahahha
    private static volatile boolean dubboTaskStarted = false;
    private static volatile boolean dubboRestTaskStarted = false;
    private static volatile boolean feignTaskStarted = false;

    /**
     * dubbo
     */
    public static void registerAndGet4Dubbo() {
        final Request request = register("dubbo", MockConfigRegistry.registryValues());
        syncDubboConfigsFromServer(request);
        if(dubboTaskStarted){
            return;
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                syncDubboConfigsFromServer(request);
            }
        }, 0, 5000L);

        dubboTaskStarted=true;
    }


    /**
     * dubbo rest
     */
    public static void registerAndGet4DubboRest() {
        final Request request = register("dubborest", DubboRestMockConfigRegistry.registryValues());
        syncDubboRestConfigsFromServer(request);

        if(dubboRestTaskStarted){
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                syncDubboRestConfigsFromServer(request);
            }
        }, 0L, 5000L);

        dubboRestTaskStarted=true;

    }


    /**
     * feign
     */
    public static void registerAndGet4Feign() {

        if(feignTaskStarted){
            return;
        }
        final Request request = register("feign", FeignMockConfigRegistry.registryValues());
        syncFeignConfigsFromServer(request);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                syncFeignConfigsFromServer(request);

            }
        }, 0, 5000L);

        feignTaskStarted=true;
    }


    private static void syncDubboRestConfigsFromServer(Request request) {
        final String result = HttpUtil.doPost(request, queryAll, 3000);
        String md5Hex = DigestUtils
                .getMD5(result).toUpperCase();

        //数据有变化
        if (!md5Hex.equals(DubboRestMockConfigRegistry.lastMd5)) {
            DubboRestMockConfigRegistry.lastMd5 = md5Hex;
            final Response response = JSON.parseObject(result, Response.class);
            DubboRestMockConfigRegistry.sync(response.getMockConfigs());
        }
    }


    private static void syncDubboConfigsFromServer(Request request) {
        final String result = HttpUtil.doPost(request, queryAll, 3000);
        String md5Hex = DigestUtils
                .getMD5(result).toUpperCase();

        //数据有变化
        if (!md5Hex.equals(MockConfigRegistry.lastMd5)) {
            MockConfigRegistry.lastMd5 = md5Hex;
            final Response response = JSON.parseObject(result, Response.class);
            MockConfigRegistry.sync(response.getMockConfigs());
        }
    }




    private static void syncFeignConfigsFromServer(Request request) {
        final String result = HttpUtil.doPost(request, queryAll, 3000);
        String md5Hex = DigestUtils
                .getMD5(result).toUpperCase();

        //数据有变化
        if (!md5Hex.equals(FeignMockConfigRegistry.lastMd5)) {
            FeignMockConfigRegistry.lastMd5 = md5Hex;
            final Response response = JSON.parseObject(result, Response.class);
            FeignMockConfigRegistry.sync(response.getMockConfigs());
        }
    }


    private static Request register(String type,List<MockConfig> mockConfigs) {
        Request request = new Request();
        request.setType(type);
        request.setAppName(Global.applicationName);
        request.setMockConfigList(mockConfigs);

        try {
            //注册
            HttpUtil.doPost(request, register, 3000);
        }catch (Exception ex){
            if(Global.agentMandatory){
                throw new RuntimeException("注册请求失败，mock.agent.mandatory 设置为true, 程序将退出");
            }
        }
        return request;
    }

}
