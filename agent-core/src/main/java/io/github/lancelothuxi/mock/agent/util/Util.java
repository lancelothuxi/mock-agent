package io.github.lancelothuxi.mock.agent.util;

import java.io.*;
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

    public static String readFileToString(File file) throws IOException {
        // 创建文件对象
        if (!file.exists()) {
            throw new IOException("文件不存在");
        }

        // 读取文件内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        // 返回文件内容
        return sb.toString();
    }
}
