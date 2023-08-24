package io.github.lancelothuxi.mock.agent.util;

import static io.github.lancelothuxi.mock.agent.config.Constant.FALSE_STR;
import static io.github.lancelothuxi.mock.agent.config.Constant.LEFT_BRACKET;
import static io.github.lancelothuxi.mock.agent.config.Constant.LEFT_SQUARE_BRACKET;
import static io.github.lancelothuxi.mock.agent.config.Constant.NULL_STR;
import static io.github.lancelothuxi.mock.agent.config.Constant.TRUE_STR;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

/** @author lancelot */
public class ParseUtil {

    public static Object parseMockValue(String mock, Type type) {
        Object value;
        if (NULL_STR.equals(mock)) {
            value = null;
        } else if (TRUE_STR.equals(mock)) {
            value = true;
        } else if (FALSE_STR.equals(mock)) {
            value = false;
        } else if (mock.length() < 2
            || (!mock.startsWith("\"") || !mock.endsWith("\"")) && (!mock.startsWith("'") || !mock.endsWith("'"))) {
            if (type != null && type.getTypeName().equals("java.lang.String")) {
                value = mock;
            } else if (StringUtils.isNumeric(mock)) {
                value = JSON.parseObject(mock);
            } else if (mock.startsWith(LEFT_BRACKET)) {
                value = JSON.parseObject(mock, type);
            } else if (mock.startsWith(LEFT_SQUARE_BRACKET)) {
                value = JSON.parseObject(mock, type);
            } else {
                value = mock;
            }
        } else {
            value = mock.subSequence(1, mock.length() - 1);
        }

        return value;
    }
}
