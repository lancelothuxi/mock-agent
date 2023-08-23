package io.github.lancelothuxi.mock.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/17 下午5:56
 */
public class LogUtil {

    private static Logger logger= LoggerFactory.getLogger(LogUtil.class);

    private static boolean logEnabled = Objects.equals("true", System.getProperty("mock.agent.log.enabled"));

    public static void log(String message, Object... args) {
        if (!logEnabled) {
            return;
        }
        logger.info(message,args);
    }
}
