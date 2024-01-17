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
 * @since 2023/8/24 下午4:33
 */
public class PluginBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(PluginBootstrap.class);

    public List<Transformer> loadPlugins() {

        // load plugins
        PluginResourcesResolver pluginResourcesResolver = new PluginResourcesResolver();
        List<URL> resources = pluginResourcesResolver.getResources();

        if (resources == null || resources.isEmpty()) {
            return new ArrayList<>();
        }

        return loadPlugins(resources);
    }

    private List<Transformer> loadPlugins(List<URL> resources) {
        List<Transformer> transformers = new ArrayList<>();
        for (URL url : resources) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        if (line.trim().isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        PluginDefine pluginDefine = PluginDefine.build(line);
                        Transformer transformer = (Transformer) Class.forName(pluginDefine.getDefineClass()).newInstance();
                        transformers.add(transformer);
                    } catch (Exception e) {
                        logger.error("mock-agent 加载插件出错", e);
                    }
                }

            } catch (IOException e) {
                logger.error("mock-agent 加载插件出错", e);
            }
        }

        return transformers;
    }
}
