package io.github.lancelothuxi.mock.agent.config;

import io.github.lancelothuxi.mock.agent.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.lancelothuxi.mock.agent.util.ConfigUtil.getPropertyFromEnvOrSystemProperty;

/**
 * @author lancelot
 */
public class GlobalConfig {

    public static volatile String mockServerURL;
    public static volatile String applicationName;
    /**
     * 当agent出现错误时候，是否降级执行真实调用，否则抛出异常
     */
    public static boolean degrade = false;
    private static Logger logger = LoggerFactory.getLogger(GlobalConfig.class);

    public static void init() {
        degrade = "true".equals(System.getProperty(Constant.AGENT_DEGRADE));
        getServerUrl();
        getApplicationName();
        logger.info("mock agent env mockServerURL={} applicationName={}", mockServerURL, applicationName);
    }

    private static void getApplicationName() {
        String mockAgentApplicationName = getPropertyFromEnvOrSystemProperty("MOCK_APPLICATION_NAME");
        if (StringUtils.isNotEmpty(mockAgentApplicationName)) {
            applicationName = mockAgentApplicationName;
        } else {
            throw new RuntimeException("please config MOCK_APPLICATION_NAME as OS or System variable");
        }
    }

    private static void getServerUrl() {

        String mockServerUrlFromEnv = getPropertyFromEnvOrSystemProperty("MOCK_SERVER_URL");
        if (StringUtils.isNotEmpty(mockServerUrlFromEnv)) {
            mockServerURL = mockServerUrlFromEnv;
        } else {
            throw new RuntimeException("please config MOCK_SERVER_URL as OS or System variable");
        }
    }
}
