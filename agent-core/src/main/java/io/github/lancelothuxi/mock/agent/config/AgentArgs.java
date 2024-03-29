package io.github.lancelothuxi.mock.agent.config;


import io.github.lancelothuxi.mock.agent.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AgentArgs {

    private Map<String, String> parameterMap=new HashMap<>();

    public AgentArgs(String agentArgs) {
        parseArgs(agentArgs);
    }

    private void parseArgs(String agentArgs) {
        if(StringUtils.isEmpty(agentArgs)){
            return;
        }

        for (String param : agentArgs.split("&")) {
            String[] pair = param.split("=");
            parameterMap.put(pair[0], pair[1].trim()); // Handle multiple values for a parameter
        }
    }

    public String getConfig(String key){
        return parameterMap.get(key);
    }
}
