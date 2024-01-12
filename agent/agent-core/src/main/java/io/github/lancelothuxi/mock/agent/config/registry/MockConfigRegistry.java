package io.github.lancelothuxi.mock.agent.config.registry;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.util.MapCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lancelot
 */
public class MockConfigRegistry {
    private static Logger logger = LoggerFactory.getLogger(MockConfigRegistry.class);
    public static String lastMd5;
    private static Map<Key, MockConfig> registry = new ConcurrentHashMap<>();
    private static Map<Key, MockConfig> registerRegistry = new ConcurrentHashMap<>();

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
            this.groupName = mockConfig.getGroupName();
            this.version = mockConfig.getVersion();
            this.type= mockConfig.getVersion();
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
