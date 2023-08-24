package io.github.lancelothuxi.mock.agent.functions;

import java.util.Collection;

public abstract class AbstractFunction implements Function {

    /**
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public abstract String execute(Object... args) throws Exception;

    /**
     * @param parameters
     * @throws Exception
     */
    @Override
    public abstract void setParameters(Collection<CompoundVariable> parameters) throws Exception;

    /** @return */
    @Override
    public abstract String getReferenceKey();
}
