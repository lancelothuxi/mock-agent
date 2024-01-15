package io.github.lancelothuxi.mock.agent.dubbo.alibaba;

import io.github.lancelothuxi.mock.agent.core.Plugin;
import io.github.lancelothuxi.mock.agent.core.Transformer;

import java.util.Arrays;
import java.util.List;

public class DubboAlibabaPlugin implements Plugin {
    @Override
    public List<Transformer> transformers() {
        return Arrays.asList(new RegisterTransformer(),new InvokerTransformer());
    }
}
