package io.github.lancelothuxi.mock.agent.config.registry;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.polling.MockConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.local.LocalFileConfigFetcher;
import io.github.lancelothuxi.mock.agent.polling.remote.HttpConfigFetcher;
import io.github.lancelothuxi.mock.agent.util.MapCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.github.lancelothuxi.mock.agent.config.GlobalConfig.CONFIG_MODE;
import static io.github.lancelothuxi.mock.agent.util.ConfigUtil.getPropertyFromEnvOrSystemProperty;
import static io.github.lancelothuxi.mock.agent.util.Util.getOrDefault;

/**
 * @author lancelot
 */
public class MockConfigRegistry {
    private static final Logger logger = LoggerFactory.getLogger(MockConfigRegistry.class);
    public static String lastMd5;
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


        List<MockConfig> mockConfigs = mockConfigFetcher.getMockConfigs();
        sync(mockConfigs);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<MockConfig> mockConfigs = mockConfigFetcher.getMockConfigs();
                sync(mockConfigs);
            }
        }, 5000L, 5000L);
    }
    public static void add(MockConfig mockConfig) {
        Key key = new Key(mockConfig);
        registry.put(key, mockConfig);
    }

    public static void add4Register(MockConfig mockConfig) {
        Key key = new Key(mockConfig);
        registerRegistry.put(key, mockConfig);
    }

    public static Set<Key> keys() {
        return registry.keySet();
    }

    /**
     * 同步到到本地存储
     *
     * @param configs
     */
    public static void sync(List<MockConfig> configs) {

        if (configs == null || configs.isEmpty()) {
            registry.clear();
        }

        Map<Key, MockConfig> tmp = new HashMap<>();
        assert configs != null;
        for (MockConfig config : configs) {
            Key key = new Key(config);
            tmp.put(key, config);
        }

        MapCompare.compareMaps(registry, tmp);
    }

    public static List<MockConfig> getMockConfigs(String type) {
        return registry.values().stream().filter(t->type.equals(t.getType())).collect(Collectors.toList());
    }


    public static List<MockConfig> registryValues() {
        return new ArrayList<>(registerRegistry.values());
    }

    public static MockConfig getMockConfig(MockConfig query) {

        Key key = new Key(query);

        final MockConfig mockConfig = registry.get(key);

        if (mockConfig == null) {
            logger.info("mock agent query mock config from registry interfaceName={} methodName={} "
                            + "group={} version={}  current keys size ={}",
                    query.getInterfaceName(), query.getMethodName(), query.getGroupName(), query.getVersion(),
                    keys().size());
        }

        return mockConfig;
    }

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

        public String getInterfaceName() {
            return interfaceName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getVersion() {
            return version;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(type, key.type) && Objects.equals(interfaceName, key.interfaceName) && Objects.equals(methodName, key.methodName) && Objects.equals(groupName, key.groupName) && Objects.equals(version, key.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, interfaceName, methodName, groupName, version);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "type='" + type + '\'' +
                    ", interfaceName='" + interfaceName + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", groupName='" + groupName + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}
