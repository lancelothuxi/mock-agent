package io.github.lancelothuxi.mock.agent.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/25 下午1:47
 */
public class DigestUtils {

    public static String getMD5(String input) {
        try {
            // 创建 MessageDigest 对象，指定使用 MD5 算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = input.getBytes();

            // 计算摘要
            byte[] digestBytes = md.digest(inputBytes);

            // 将摘要转换为十六进制字符串
            BigInteger num = new BigInteger(1, digestBytes);
            StringBuilder sb = new StringBuilder(num.toString(16));
            while (sb.length() < 32) {
                sb.insert(0, "0");
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
