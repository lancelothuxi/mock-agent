package io.github.lancelothuxi.mock.agent.express.ognl;

import io.github.lancelothuxi.mock.agent.express.Expression;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlExpression implements Expression {

    private OgnlContext defaultContext;

    public OgnlExpression() {
    }

    public OgnlExpression(Object[] args) {
         this.defaultContext = Ognl.createDefaultContext(args);
    }

    @Override
    public Object getValue(Object[] args, String expression)  {
        try {
            return Ognl.getValue(expression,defaultContext,args);
        } catch (OgnlException e) {
            return null;
        }
    }

    @Override
    public boolean match(Object[] args, String expression) {
        return false;
    }
}
