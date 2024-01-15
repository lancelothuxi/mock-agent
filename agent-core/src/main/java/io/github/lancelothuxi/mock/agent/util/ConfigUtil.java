package io.github.lancelothuxi.mock.agent.util;

/**
 * @author lancelot
 */
public class ConfigUtil {

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
}
