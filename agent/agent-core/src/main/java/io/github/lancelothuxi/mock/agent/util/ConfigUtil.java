package io.github.lancelothuxi.mock.agent.util;


/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigUtil {

    private int connectTimeout = 1000;
    private int readTimeout = 5000;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }


}
