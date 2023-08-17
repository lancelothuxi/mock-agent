package io.github.lancelothuxi.mock.agent;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/17 下午5:56
 */
public class LogUtil {

    private static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    private static boolean logEnabled = Objects.equals("true", System.getProperty("mock.agent.log.enabled"));

    public static void log(String message, Object... args) {
        if(!logEnabled){
            return;
        }

        if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable) {
            Throwable throwable = (Throwable) args[args.length - 1];
            throwable.printStackTrace();
        }

        String formatedMessage = MessageFormatter.format(message, args);
        try {
            bw.write(formatedMessage);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
