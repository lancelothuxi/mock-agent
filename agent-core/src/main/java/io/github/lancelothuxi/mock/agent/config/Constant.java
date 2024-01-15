package io.github.lancelothuxi.mock.agent.config;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/21 下午5:01
 */
public class Constant {

    /**
     * 当agent出现错误时候，是否降级执行真实调用，否则抛出异常
     */
    public static final String AGENT_DEGRADE = "mock.agent.degrade";

    public static final String NULL_STR = "null";

    public static final String TRUE_STR = "true";

    public static final String FALSE_STR = "false";

    public static final String LEFT_BRACKET = "{";

    public static final String RIGHT_BRACKET = "}";

    public static final String LEFT_SQUARE_BRACKET = "[";
}
