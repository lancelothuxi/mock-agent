package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import com.ea.agentloader.AgentLoader;
import io.github.lancelothuxi.mock.agent.core.Agent;
import org.mockito.Mockito;
import org.testng.annotations.Test;

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

        Result result = new MockClusterInvoker().invoke(invocation);

        Object resultValue = result.getValue();

        System.out.println("resultValue = " + resultValue);
    }
}