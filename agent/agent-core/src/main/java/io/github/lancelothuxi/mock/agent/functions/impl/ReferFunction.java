package io.github.lancelothuxi.mock.agent.functions.impl;

import com.alibaba.fastjson.JSONPath;
import io.github.lancelothuxi.mock.agent.functions.AbstractFunction;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;

import java.util.Collection;

/**
 * @author lancelot
 */
public class ReferFunction extends AbstractFunction {

    private static final String KEY = "__refer";
    private CompoundVariable[] values;

    @Override
    public String execute(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }

        String requestData = (String) args[0];
        final String referJsonPathExpression = values[0].execute();

        try {
            final Object jsonPathValue = JSONPath.read(requestData, referJsonPathExpression);
            if (jsonPathValue == null) {
                return "";
            }

            return jsonPathValue.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) {
        values = parameters.toArray(new CompoundVariable[parameters.size()]);
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }
}
