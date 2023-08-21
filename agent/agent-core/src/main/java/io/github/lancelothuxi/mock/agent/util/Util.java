package io.github.lancelothuxi.mock.agent.util;

import java.util.Collection;
import java.util.Map;

public class Util {


    public static <K, V> V mapGetOrDefault(Map<K, V> map, K key, V defautlVal) {
        final V v = map.get(key);
        if (v == null) {
            return defautlVal;
        }
        return v;
    }

    /**
     * 判断class是否存在
     *
     * @param className
     * @param classLoader
     * @return
     */
    public static boolean isClassExists(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static boolean isJavaVersionThanJdk7() {
        String javaVersion = System.getProperty("java.version");
        int majorVersion = Integer.parseInt(javaVersion.split("\\.")[0]);
        if (majorVersion > 1 || (majorVersion == 1 && Integer.parseInt(javaVersion.split("\\.")[1]) > 7)) {
            return true;
        } else {
            return false;
        }
    }

    public static String collectionToString(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return "[]";
        }

        StringBuffer stringBuffer = new StringBuffer("[");
        for (Object o : collection) {
            if (o == null) {
                stringBuffer.append("null");
                continue;
            }
            stringBuffer.append(o.toString())
                    .append(",");
        }

        stringBuffer.append("]");
        return stringBuffer.toString();
    }

}
