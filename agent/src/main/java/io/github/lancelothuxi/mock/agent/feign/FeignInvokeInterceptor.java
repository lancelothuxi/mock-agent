package io.github.lancelothuxi.mock.agent.feign;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.Global;
import io.github.lancelothuxi.mock.agent.Interceptor;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.functions.FunctionCache;
import io.github.lancelothuxi.mock.agent.config.registry.FeignMockConfigRegistry;
import io.github.lancelothuxi.mock.agent.util.ParseUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static io.github.lancelothuxi.mock.agent.polling.Util.getMockData;

/**
 * @author liulei1971
 * @date 2023/5/30 20:39
 */
public class FeignInvokeInterceptor implements Interceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static volatile CommonMockFeignClient commonMockFeignClient;

    static {

        //移除掉序列化反序列化字段不匹配抛出异常 特性
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public Object intercept(Method method, Object[] allArguments, Object self, Callable supercall) throws Exception {

        try {
            Method realMethod = (Method) allArguments[1];

            String methodName = realMethod.getName();
            if ("equals".equals(methodName) || "hashCode".equals(methodName) || "toString".equals(methodName)) {
                return supercall.call();
            }

            String interfacename = realMethod.getDeclaringClass().getName();

            if (interfacename.equals(CommonMockFeignClient.class.getName())) {
                return supercall.call();
            }


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
            query.setAppliactionName(Global.applicationName);

            final MockConfig mockConfig = FeignMockConfigRegistry.getMockConfig(query);
            if (mockConfig == null) {
                return supercall.call();
            }

            if (mockConfig.mockFromServer()) {
                if (commonMockFeignClient == null) {
                    synchronized (FeignInvokeInterceptor.class) {
                        commonMockFeignClient = Feign.builder()
                                .encoder(new JacksonEncoder())
                                .decoder(new JacksonDecoder())
                                .client(Global.feignClient)
                                .contract(new Contract.Default())
                                .target(CommonMockFeignClient.class, Global.mockServerURL);
                    }
                }

                MockFeignResponse mockFeignResponse = commonMockFeignClient.doMock(mockConfig);
                LogUtil.log("mock-agent feign mock request interfaceName={} methodName={} args={}", interfacename, methodName,argsString);

                if (mockFeignResponse == null ) {
                    LogUtil.log("mock-agent feign mock response is null interfaceName={} methodName={}", interfacename, methodName);
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }

                if (!mockFeignResponse.success()) {
                    LogUtil.log("mock-agent feign mock response is not success interfaceName={} methodName={} response={}", interfacename, methodName, mockFeignResponse.getData());
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }

                LogUtil.log("mock-agent feign mock response is valid interfaceName={} methodName={} response={}", interfacename, methodName, mockFeignResponse.getData());

                String data = mockFeignResponse.getData();
                if (data == null || data.length() == 0) {
                    throw new RuntimeException("mock agent 获取数据为空或者异常");
                }

                JavaType javaType = OBJECT_MAPPER.constructType(realMethod.getGenericReturnType());
                Object result = ParseUtil.parseMockValue(data, javaType);
                return result;
            }


            final List<MockData> mockDataList = mockConfig.getMockDataList();
            if (CollectionUtils.isEmpty(mockDataList)) {
                throw new RuntimeException("mock agent 获取数据为空或者异常");
            }

            MockData mockData = getMockData(mockDataList, argsString);
            if (mockData == null) {
                throw new RuntimeException("mock agent 获取数据为空或者异常");
            }

            if (mockData.getTimeout() != null && mockData.getTimeout() > 0) {
                Thread.sleep(mockData.getTimeout());
            }

            JavaType javaType = OBJECT_MAPPER.constructType(realMethod.getGenericReturnType());

            String data = mockData.getData();

            CompoundVariable compoundVariable = FunctionCache.get(data);
            if (compoundVariable == null) {
                compoundVariable = new CompoundVariable();
                compoundVariable.setParameters(data);
                FunctionCache.putIfAbsent(data, compoundVariable);
            }

            data = compoundVariable.execute(argsString);
            final Object result = ParseUtil.parseMockValue(data, javaType);
            return result;
        } catch (Throwable e) {
            LogUtil.log("mock-agent call dubbo rest mock has error Global.agentMandatory={}",Global.agentMandatory, e);
            if(Global.agentMandatory){
                throw e;
            }else {
                return supercall.call();
            }
        }
    }
}
