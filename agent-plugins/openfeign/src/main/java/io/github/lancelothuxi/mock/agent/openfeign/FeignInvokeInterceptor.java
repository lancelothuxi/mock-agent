package io.github.lancelothuxi.mock.agent.openfeign;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.core.Interceptor;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.mock.CommonMockService;
import io.github.lancelothuxi.mock.agent.polling.Util;
import io.github.lancelothuxi.mock.agent.util.CollectionUtils;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author liulei1971
 * @date 2023/5/30 20:39
 */
public class FeignInvokeInterceptor extends CommonMockService implements Interceptor {
    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) throws Exception {
        Method realMethod = (Method) allArguments[1];

        String methodName = realMethod.getName();
        if ("equals".equals(methodName) || "hashCode".equals(methodName) || "toString".equals(methodName)) {
            return supercall.call();
        }

        String interfacename = realMethod.getDeclaringClass().getName();

        Object[] args = (Object[]) allArguments[2];
        final String argsString = JSON.toJSONString(args);

        MockConfig query = new MockConfig();
        query.setInterfaceName(interfacename);
        query.setEnabled(1);
        query.setMethodName(methodName);
        query.setGroupName("");
        query.setVersion("");
        query.setType("feign");
        query.setData(argsString);
        query.setApplicationName(GlobalConfig.applicationName);

        return super.doMock(interfacename, methodName, "", "", supercall, argsString,
                method.getGenericReturnType());
    }

    @Override
    protected String getType() {
        return "feign";
    }
}
