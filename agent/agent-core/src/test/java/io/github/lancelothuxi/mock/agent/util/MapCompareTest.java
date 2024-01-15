package io.github.lancelothuxi.mock.agent.util;


import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/24 下午8:12
 */
public class MapCompareTest {

    @Test
    public void testMinus() {

        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        // 添加一些示例元素
        map1.put("A", 1);
        map1.put("B", 2);
        map1.put("C", 3);
        map1.put("G", 3);

        map2.put("B", 2);
        map2.put("C", 4);
        map2.put("D", 5);
        map2.put("F", 5);
        System.out.println("初始Map1: " + map1);
        System.out.println("初始Map2: " + map2);

        MapCompare.compareMaps(map1, map2);

        System.out.println("比较后的Map1: " + map1);
        System.out.println("比较后的Map2: " + map2);
    }
}
