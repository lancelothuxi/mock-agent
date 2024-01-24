package io.github.lancelothuxi.mock.agent.express;

public interface Express {

    public Object getValue(Object rootObject,String expression );

    public void setValue( Object rootObject, Object value,String expression );

}
