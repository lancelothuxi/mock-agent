package io.github.lancelothuxi.mock.agent.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/24 下午7:46
 */
public class MapCompare {

    public static <K, V> void compareMaps(Map<K, V> map1, Map<K, V> map2) {
        // 找出需要删除的元素
        Map<K, V> toRemove = new HashMap<>();
        for (Map.Entry<K, V> entry : map1.entrySet()) {
            if (!map2.containsKey(entry.getKey()) || !map2.get(entry.getKey()).equals(entry.getValue())) {
                toRemove.put(entry.getKey(), entry.getValue());
            }
        }

        // 找出需要添加的元素
        Map<K, V> toAdd = new HashMap<>();
        for (Map.Entry<K, V> entry : map2.entrySet()) {
            if (!map1.containsKey(entry.getKey()) || !map1.get(entry.getKey()).equals(entry.getValue())) {
                toAdd.put(entry.getKey(), entry.getValue());
            }
        }

        // 删除不需要的元素
        for (Map.Entry<K, V> entry : toRemove.entrySet()) {
            map1.remove(entry.getKey());
        }

        // 添加缺失的元素
        map1.putAll(toAdd);
    }
}
