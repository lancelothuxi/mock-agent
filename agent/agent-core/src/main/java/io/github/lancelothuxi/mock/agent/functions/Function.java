package io.github.lancelothuxi.mock.agent.functions;

import java.util.Collection;

/** @author lancelot */
public interface Function {

    /** @return */
    String getReferenceKey();

    /**
     * @param parameters
     * @throws Exception
     */
    void setParameters(Collection<CompoundVariable> parameters) throws Exception;

    /**
     * @param args
     * @return
     * @throws Exception
     */
    String execute(Object... args) throws Exception;
}
