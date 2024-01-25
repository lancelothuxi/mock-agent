package io.github.lancelothuxi.mock.agent.express;

import ognl.OgnlException;

public interface Expression {

    public Object getValue(Object[] args,String expression );

    public boolean match(Object[] args,String expression );


}
