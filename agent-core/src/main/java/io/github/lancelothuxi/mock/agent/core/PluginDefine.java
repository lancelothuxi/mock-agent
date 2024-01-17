package io.github.lancelothuxi.mock.agent.core;

import io.github.lancelothuxi.mock.agent.util.StringUtils;

/**
 * @author lancelot
 * @version 1.0
 * @since  2023/8/24 下午4:02
 */
public class PluginDefine {

    /**
     * Plugin name.
     */
    private String name;

    /**
     * The class name of plugin defined.
     */
    private String defineClass;

    private PluginDefine(String name, String defineClass) {
        this.name = name;
        this.defineClass = defineClass;
    }

    public static PluginDefine build(String define) throws IllegalStateException {
        if (StringUtils.isEmpty(define)) {
            throw new IllegalStateException(define);
        }

        String[] pluginDefine = define.split("=");
        if (pluginDefine.length != 2) {
            throw new IllegalStateException(define);
        }

        String pluginName = pluginDefine[0];
        String defineClass = pluginDefine[1];
        return new PluginDefine(pluginName, defineClass);
    }

    public String getDefineClass() {
        return defineClass;
    }

    public String getName() {
        return name;
    }
}
