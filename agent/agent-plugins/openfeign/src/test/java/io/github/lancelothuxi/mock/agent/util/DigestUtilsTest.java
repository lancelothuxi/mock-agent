package io.github.lancelothuxi.mock.agent.util;

import org.testng.annotations.Test;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/25 下午1:50
 */
public class DigestUtilsTest {

    @Test
    public void testGetMD5() {

        String input = "Hello World";
        String md5 = DigestUtils.getMD5(input);
        System.out.println("MD5: " + md5);
    }
}
