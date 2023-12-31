package com.alibaba.dubbo.rpc.cluster.support.wrapper;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import io.github.lancelothuxi.mock.agent.dubbo.alibaba.DemoInterface;


/**
 * 创建一个同包名 同类名的MockClusterInvoker的 方便测试
 * @author lancelot
 * @version 1.0
 * @date 2023/8/25 上午11:41
 */
public class MockClusterInvoker {

    public Result invoke(Invocation invocation) throws RpcException{
        return new RpcResult("real data");
    }

    public Class getInterface(){
        return DemoInterface.class;
    }

    public URL getUrl() {
        return new URL("dubbo","localhost",1234);
    }
}
