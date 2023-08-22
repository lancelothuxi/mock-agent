package io.github.lancelothuxi.mock.agent.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午8:09
 */
class MyParameterizedType implements ParameterizedType {
    private final Class<?> rawType;
    private final Type[] actualTypeArguments;

    public MyParameterizedType(Class<?> rawType, Type... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rawType.getSimpleName());
        if (actualTypeArguments.length > 0) {
            sb.append("<");
            for (int i = 0; i < actualTypeArguments.length; i++) {
                sb.append(actualTypeArguments[i].getTypeName());
                if (i < actualTypeArguments.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(">");
        }
        return sb.toString();
    }
}