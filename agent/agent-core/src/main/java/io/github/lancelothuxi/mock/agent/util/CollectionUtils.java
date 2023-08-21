package io.github.lancelothuxi.mock.agent.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午3:03
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
}
