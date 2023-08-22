package io.github.lancelothuxi.mock.agent.config;

import io.github.lancelothuxi.mock.agent.util.StringUtils;

/**
 * @author lancelot
 */
public class GlobalConfig {

    public static volatile String mockServerURL;
    public static volatile String zkAddress;
    public static volatile String applicationName;

    /**
     * 当agent出现错误时候，是否降级执行真实调用，否则抛出异常
     */
    public static boolean degrade = false;

    static {
        getServerUrl();
    }

    public static void init() {
        degrade = "true".equals(System.getProperty(Constant.AGENT_DEGRADE));
    }

    private static void getServerUrl() {

        String mockServerUrlFromEnv = getPropertyFromEnvOrSystemProperty("MOCK_SERVER_URL");
        if (StringUtils.isNotEmpty(mockServerUrlFromEnv)) {
            mockServerURL = mockServerUrlFromEnv;
        } else {
            throw new RuntimeException("get MOCK_SERVER_URL from env failed");
        }

        String zkAddressFromEnv = getPropertyFromEnvOrSystemProperty("ZK_ADDRESS");
        if (StringUtils.isNotEmpty(zkAddressFromEnv)) {
            zkAddress = zkAddressFromEnv;
        } else {
            throw new RuntimeException("get ZK_ADDRESS from env failed");
        }
    }


    private static String getPropertyFromEnvOrSystemProperty(String key) {

        String value = System.getenv(key);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        String propertyValue = System.getProperty(key);
        if (StringUtils.isNotEmpty(propertyValue)) {
            return propertyValue;
        }

        return null;
    }
}
