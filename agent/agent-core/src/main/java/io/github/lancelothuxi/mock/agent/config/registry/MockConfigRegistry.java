package io.github.lancelothuxi.mock.agent.config.registry;

import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.util.MapCompare;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

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

        if (configs == null || configs.size() == 0) {
            registry.clear();
        }

        Map<Key, MockConfig> tmp = new HashMap<>();
        for (MockConfig config : configs) {
            Key key =
                    new Key(config);
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
            LogUtil.log(
                    "mock agent query mock config from registry interfaceName={} methodName={} "
                            + "group={} version={}  current keys size ={}",
                    query.getInterfaceName(), query.getMethodName(), query.getGroupName(), query.getVersion(),
                    keys().size());
        }

        return mockConfig;
    }

    public static MockConfig getMockConfig(Key key) {

        if (!registry.containsKey(key)) {
            return null;
        }

        final MockConfig mockConfig = registry.get(key);

        return mockConfig;
    }

    public static class Key {

        /**
         * 类型
         */
        private String type;
        /**
         * 接口
         */
        private String interfaceName;
        /**
         * 方法
         */
        private String methodName;
        /**
         * 分组
         */
        private String groupName;
        /**
         * 版本
         */
        private String version;

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
