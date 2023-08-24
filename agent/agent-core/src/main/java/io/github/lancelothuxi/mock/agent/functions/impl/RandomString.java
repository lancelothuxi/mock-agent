package io.github.lancelothuxi.mock.agent.functions.impl;

import java.util.Collection;

import io.github.lancelothuxi.mock.agent.functions.AbstractFunction;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;
import io.github.lancelothuxi.mock.agent.util.RandomStringUtils;
import io.github.lancelothuxi.mock.agent.util.StringUtils;

/** @author lancelot */
public class RandomString extends AbstractFunction {

    private static final String KEY = "__RandomString";
    private static final int CHARS = 2;
    private CompoundVariable[] values;

    @Override
    public String execute(Object... args) {
        int length = Integer.parseInt(values[0].execute());
        String charsToUse = null;
        if (values.length >= CHARS) {
            charsToUse = values[CHARS - 1].execute().trim();
            if (charsToUse.length() <= 0) {
                charsToUse = null;
            }
        }

        String myValue;
        if (StringUtils.isEmpty(charsToUse)) {
            myValue = RandomStringUtils.randomAlphabetic(length);
        } else {
            myValue = RandomStringUtils.random(length, charsToUse);
        }

        return myValue;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) {
        // TODO 参数个数校验
        values = parameters.toArray(new CompoundVariable[parameters.size()]);
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }
}
