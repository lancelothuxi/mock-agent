package io.github.lancelothuxi.mock.agent.spring;

import io.github.lancelothuxi.mock.agent.core.Plugin;
import io.github.lancelothuxi.mock.agent.core.Transformer;

import java.util.Arrays;
import java.util.List;

public class SpringPlugin implements Plugin {
    @Override
    public List<Transformer> transformers() {
        return Arrays.asList(new SpringApplicationNameTransformer());
    }
}
