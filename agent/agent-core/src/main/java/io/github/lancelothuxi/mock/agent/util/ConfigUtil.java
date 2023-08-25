package io.github.lancelothuxi.mock.agent.util;

/**
 * @author lancelot
 */
public class ConfigUtil {

    private int connectTimeout = 1000;
    private int readTimeout = 5000;

    public static String getPropertyFromEnvOrSystemProperty(String key) {

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

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }
}
