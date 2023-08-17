package io.github.lancelothuxi.mock.agent.functions;

import java.util.Collection;

public abstract class AbstractFunction implements Function {

    @Override
    abstract public String execute(Object... args) throws Exception;

    @Override
    abstract public void setParameters(Collection<CompoundVariable> parameters) throws Exception;

    @Override
    abstract public String getReferenceKey();

}
