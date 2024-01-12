package io.github.lancelothuxi.mock.agent.util;

import com.alibaba.fastjson.JSON;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Type;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午7:38
 */
public class ParseUtilTest {

    @Test
    public void testParseMockValue() {
        Object result = ParseUtil.parseMockValue("mock", null);
        Assert.assertEquals(result, "mock");
    }

    @Test
    public void testParseNull() {
        Object result = ParseUtil.parseMockValue("null", null);
        Assert.assertNull(result);
    }
}
