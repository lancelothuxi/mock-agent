package io.github.lancelothuxi.mock.agent.express.jsonpath;

import com.alibaba.fastjson2.JSONPath;
import io.github.lancelothuxi.mock.agent.express.Expression;

public class JsonpathExpression implements Expression {

    public JsonpathExpression() {
    }

    public JsonpathExpression(Object[] args) {
    }

    @Override
    public Object getValue(Object[] args, String expression) {
        return JSONPath.eval(args,expression);
    }

    @Override
    public boolean match(Object[] args, String expression) {
        return false;
    }
}
