package io.github.lancelothuxi.mock.agent.config.registry;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.QueryMockConfigsRequest;
import io.github.lancelothuxi.mock.agent.polling.local.LocalFileConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.remote.HttpConfigFetcher;
import io.github.lancelothuxi.mock.agent.util.MapCompare;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.github.lancelothuxi.mock.agent.config.GlobalConfig.CONFIG_MODE;
import static io.github.lancelothuxi.mock.agent.config.GlobalConfig.applicationName;
import static io.github.lancelothuxi.mock.agent.util.ConfigUtil.getPropertyFromEnvOrSystemProperty;
import static io.github.lancelothuxi.mock.agent.util.Util.getOrDefault;

/**
 * @author lancelot
 */
public class MockConfigRegistry {
    private static final Logger logger = LoggerFactory.getLogger(MockConfigRegistry.class);
    private static final Map<Key, MockConfig> registry = new ConcurrentHashMap<>();
    private static final Map<Key, MockConfig> registerRegistry = new ConcurrentHashMap<>();
    private static MockConfigFetcher mockConfigFetcher;
    private static final Timer timer = new Timer();

    public static void init(){
        //init config fetcher
        String configMode = getPropertyFromEnvOrSystemProperty(CONFIG_MODE);
        if("file".equals(configMode)){
            mockConfigFetcher = new LocalFileConfigFetcher();
        }else {
            mockConfigFetcher = new HttpConfigFetcher();
        }
        QueryMockConfigsRequest mockConfigsRequest =new QueryMockConfigsRequest();
        mockConfigsRequest.setApplicationName(applicationName);
        sync(mockConfigsRequest);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sync(mockConfigsRequest);
            }
        }, 1000L, 5000L);
    }
    public static void add(MockConfig mockConfig) {
        Key key = new Key(mockConfig);
        registry.put(key, mockConfig);
    }

    public static void add4Register(MockConfig mockConfig) {
        Key key = new Key(mockConfig);
        registerRegistry.put(key, mockConfig);
    }

    /**
     * 同步到到本地存储
     *
     */
    public static void sync(QueryMockConfigsRequest mockConfigsRequest) {
        mockConfigsRequest.setMockConfigList(registerRegistry.values().stream().collect(Collectors.toList()));
        List<MockConfig> configs = mockConfigFetcher.getMockConfigs(mockConfigsRequest);
        if (configs == null || configs.isEmpty()) {
            registry.clear();
        }
        Map<Key, MockConfig> tmp = new HashMap<>();
        for (MockConfig config : configs) {
            Key key = new Key(config);
            tmp.put(key, config);
        }
        MapCompare.compareMaps(registry, tmp);
    }

    public static List<MockConfig> getMockConfigs(String type) {
        return registry.values().stream().filter(t->type.equals(t.getType())).collect(Collectors.toList());
    }

    public static MockConfig getMockConfig(MockConfig query) {

        Key key = new Key(query);

        final MockConfig mockConfig = registry.get(key);

        if (mockConfig == null) {
            if(logger.isDebugEnabled()){
                logger.info("mock agent query mock config from registry interfaceName={} methodName={} "
                                + "group={} version={}", query.getInterfaceName(), query.getMethodName(), query.getGroupName(), query.getVersion());
            }
        }
        return mockConfig;
    }

    @Data
    @EqualsAndHashCode
    public static class Key {

        /**
         * 类型
         */
        private final String type;
        /**
         * 接口
         */
        private final String interfaceName;
        /**
         * 方法
         */
        private final String methodName;
        /**
         * 分组
         */
        private final String groupName;
        /**
         * 版本
         */
        private final String version;

        public Key(MockConfig mockConfig) {
            this.interfaceName = mockConfig.getInterfaceName();
            this.methodName = mockConfig.getMethodName();
            this.groupName = getOrDefault(mockConfig.getGroupName(),"");
            this.version = getOrDefault(mockConfig.getVersion(),"");
            this.type= mockConfig.getType();
        }
    }
}
