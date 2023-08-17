package io.github.lancelothuxi.mock.agent;

import feign.Client;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

public class Global {

    public static volatile String mockServerURL;
    public static volatile String zkAddress;
    public static volatile String applicationName;
    public static volatile Client feignClient;
    public static boolean agentMandatory=true;


    static {
        getServerUrl();
    }


    private static void getServerUrl() {

        String mockServerUrlFromEnv = getPropertyFromEnvOrSystemProperty("MOCK_SERVER_URL");
        if(StringUtils.isNotEmpty(mockServerUrlFromEnv)){
            mockServerURL = mockServerUrlFromEnv;
            System.out.println("read mockServerUrlFromEnv = " + mockServerUrlFromEnv);
        }else {
            throw new RuntimeException("get MOCK_SERVER_URL from env failed");
        }

        String zkAddressFromEnv = getPropertyFromEnvOrSystemProperty("ZK_ADDRESS");
        if(StringUtils.isNotEmpty(zkAddressFromEnv)){
            zkAddress = zkAddressFromEnv;
            System.out.println("read zkAddressFromEnv = " + zkAddressFromEnv);
        }else {
            throw new RuntimeException("get ZK_ADDRESS from env failed");
        }
    }


    private static String getPropertyFromEnvOrSystemProperty(String key){
        String value = System.getenv(key);
        if(StringUtils.isNotEmpty(value)){
            return value;
        }

        String propertyValue = System.getProperty(key);
        if(StringUtils.isNotEmpty(propertyValue)){
            return propertyValue;
        }

        return null;
    }
}
