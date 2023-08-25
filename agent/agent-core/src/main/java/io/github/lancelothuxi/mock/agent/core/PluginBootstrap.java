package io.github.lancelothuxi.mock.agent.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/24 下午4:33
 */
public class PluginBootstrap {

    private static Logger logger = LoggerFactory.getLogger(PluginBootstrap.class);

    public List<Plugin> loadPlugins() {

        // load plugins
        PluginResourcesResolver pluginResourcesResolver = new PluginResourcesResolver();
        List<URL> resources = pluginResourcesResolver.getResources();

        if (resources == null || resources.size() == 0) {
            return new ArrayList<>();
        }

        return loadPlugins(resources);
    }

    private List<Plugin> loadPlugins(List<URL> resources) {
        List<Plugin> plugins = new ArrayList<>();
        for (URL url : resources) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    try {
                        if (line.trim().length() == 0 || line.startsWith("#")) {
                            continue;
                        }
                        PluginDefine pluginDefine = PluginDefine.build(line);
                        Plugin plugin = (Plugin) Class.forName(pluginDefine.getDefineClass()).newInstance();
                        plugins.add(plugin);
                    } catch (Exception e) {
                        logger.error("mock-agent 加载插件出错", e);
                    }
                }

            } catch (IOException e) {
                logger.error("mock-agent 加载插件出错", e);
            }
        }

        return plugins;
    }
}
