package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.MockData;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import io.github.lancelothuxi.mock.agent.util.DemoGeneric;
import io.github.lancelothuxi.mock.agent.util.DemoRequest;
import io.github.lancelothuxi.mock.agent.util.Person;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/25 上午11:43
 */
public class DubboStartInterceptorTest {

    @Test
    public void testIntercept() {

        Invocation invocation = Mockito.mock(Invocation.class);
        MockClusterInvoker spiedMockClusterInvoker = Mockito.spy(new MockClusterInvoker());

        when(invocation.getMethodName()).thenReturn("hello");
        when(invocation.getParameterTypes()).thenReturn(new Class[]{DemoRequest.class});

        DemoRequest demoRequest=new DemoRequest();
        when(invocation.getArguments()).thenReturn(new Object[]{demoRequest});

        Person person=new Person("tom",11);
        String jsonString = JSON.toJSONString(person);

        MockConfig mockConfig=new MockConfig();
        mockConfig.setServerSideMock(0);

        MockData mockData =new MockData();
        mockData.setData(jsonString);

        mockConfig.setMockDataList(Arrays.asList(mockData));

        mockStatic(MockConfigRegistry.class).when(() -> MockConfigRegistry.getMockConfig(any(MockConfig.class)))
                .thenReturn(mockConfig);

        Result result = spiedMockClusterInvoker.invoke(invocation);

        Object resultValue = result.getValue();

        assertTrue(resultValue instanceof Person);

        //处理泛型
        Person personResult=(Person)resultValue;
        assertEquals(personResult.getName(),"tom");
    }
}