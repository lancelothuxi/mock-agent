package io.github.lancelothuxi.mock.agent.express.ognl;

import io.github.lancelothuxi.mock.agent.express.Express;
import ognl.Ognl;
import ognl.OgnlException;

public class OgnlExpress  implements Express {
    private Object ognlExpression;
    @Override
    public Object getValue(Object rootObject, String expression) {

        try {
            Object ognlExpression = Ognl.parseExpression( expression );
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Object getExpression()
    {
        return ognlExpression;
    }


    @Override
    public void setValue(Object rootObject, Object value, String expression) {

    }
}
