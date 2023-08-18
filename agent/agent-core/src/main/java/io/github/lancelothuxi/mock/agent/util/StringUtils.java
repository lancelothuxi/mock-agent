package io.github.lancelothuxi.mock.agent.util;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/25 下午1:43
 */
public class StringUtils {

    public static final String EMPTY = "";


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }


    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
