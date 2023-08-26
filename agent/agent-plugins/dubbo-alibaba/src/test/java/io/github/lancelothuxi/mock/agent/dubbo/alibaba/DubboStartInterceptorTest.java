package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import io.github.lancelothuxi.mock.agent.util.DemoRequest;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

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

        Result result = spiedMockClusterInvoker.invoke(invocation);

        Object resultValue = result.getValue();

        System.out.println("resultValue = " + resultValue);
    }
}