package io.github.lancelothuxi.mock.agent.config.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.util.MapCompare;

public class MockConfigRegistry {

    public static String lastMd5;
    private static Map<Key, MockConfig> registry = new ConcurrentHashMap<>();
    private static Map<Key, MockConfig> registerRegistry = new ConcurrentHashMap<>();

    public static void add(MockConfig mockConfig) {
        Key key = new Key(mockConfig.getInterfaceName(), mockConfig.getMethodName(), mockConfig.getGroupName(),
            mockConfig.getVersion());
        registry.put(key, mockConfig);
    }

    public static void add4Register(MockConfig mockConfig) {
        Key key = new Key(mockConfig.getInterfaceName(), mockConfig.getMethodName(), mockConfig.getGroupName(),
            mockConfig.getVersion());
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
                new Key(config.getInterfaceName(), config.getMethodName(), config.getGroupName(), config.getVersion());
            tmp.put(key, config);
        }

        MapCompare.compareMaps(registry, tmp);
    }

    public static List<MockConfig> registryValues() {
        return new ArrayList<>(registerRegistry.values());
    }

    public static MockConfig getMockConfig(MockConfig query) {

        Key key = new Key(query.getInterfaceName(), query.getMethodName(), query.getGroupName(), query.getVersion());

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
        /** 接口 */
        private String interfaceName;
        /** 方法 */
        private String methodName;
        /** 分组 */
        private String groupName;
        /** 版本 */
        private String version;

        public Key(String interfaceName, String methodName, String groupName, String version) {
            this.interfaceName = interfaceName;
            this.methodName = methodName;
            this.groupName = groupName;
            this.version = version;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key)o;
            return Objects.equals(interfaceName, key.interfaceName) && Objects.equals(methodName, key.methodName)
                && Objects.equals(groupName, key.groupName) && Objects.equals(version, key.version);
        }

        @Override
        public String toString() {
            return "Key{" + "interfaceName='" + interfaceName + '\'' + ", methodName='" + methodName + '\''
                + ", groupName='" + groupName + '\'' + ", version='" + version + '\'' + '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(interfaceName, methodName, groupName, version);
        }
    }
}
