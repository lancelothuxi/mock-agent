package io.github.lancelothuxi.mock.agent.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/26 下午5:30
 */
public class GenericClassUtil {


    public static Type createGenericType(Class raw,Class<?> typeArgument) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { typeArgument };
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public static Class<?> getClassFromType(Class raw,Class<?> typeArgument) {
        Type type = createGenericType(raw,typeArgument);
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

}
