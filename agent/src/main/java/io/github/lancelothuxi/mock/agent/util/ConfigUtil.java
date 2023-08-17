package io.github.lancelothuxi.mock.agent.util;

import java.util.concurrent.TimeUnit;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigUtil {

    private int refreshInterval = 5;
    private TimeUnit refreshIntervalTimeUnit = TimeUnit.MINUTES;
    private int connectTimeout = 1000; //1 second
    private int readTimeout = 5000; //5 seconds
    private String cluster;
    private int loadConfigQPS = 2; //2 times per second
    private int longPollQPS = 2; //2 times per second
    //for on error retry
    private long onErrorRetryInterval = 1;//1 second
    private TimeUnit onErrorRetryIntervalTimeUnit = TimeUnit.SECONDS;//1 second
    //for typed config cache of parser result, e.g. integer, double, long, etc.
    private long maxConfigCacheSize = 500;//500 cache key
    private long configCacheExpireTime = 1;//1 minute
    private TimeUnit configCacheExpireTimeUnit = TimeUnit.MINUTES;//1 minute
    private long longPollingInitialDelayInMills = 2000;//2 seconds
    private boolean autoUpdateInjectedSpringProperties = true;


    public int getRefreshInterval() {
        return refreshInterval;
    }

    public TimeUnit getRefreshIntervalTimeUnit() {
        return refreshIntervalTimeUnit;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getCluster() {
        return cluster;
    }

    public int getLoadConfigQPS() {
        return loadConfigQPS;
    }

    public int getLongPollQPS() {
        return longPollQPS;
    }

    public long getOnErrorRetryInterval() {
        return onErrorRetryInterval;
    }

    public TimeUnit getOnErrorRetryIntervalTimeUnit() {
        return onErrorRetryIntervalTimeUnit;
    }

    public long getMaxConfigCacheSize() {
        return maxConfigCacheSize;
    }

    public long getConfigCacheExpireTime() {
        return configCacheExpireTime;
    }

    public TimeUnit getConfigCacheExpireTimeUnit() {
        return configCacheExpireTimeUnit;
    }

    public long getLongPollingInitialDelayInMills() {
        return longPollingInitialDelayInMills;
    }

    public boolean isAutoUpdateInjectedSpringProperties() {
        return autoUpdateInjectedSpringProperties;
    }

}
