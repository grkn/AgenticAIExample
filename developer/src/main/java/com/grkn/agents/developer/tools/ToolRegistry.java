package com.grkn.agents.developer.tools;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ToolRegistry {
    private final Map<String, Tool<Payload, Payload>> tools = new ConcurrentHashMap<>();

    public ToolRegistry(List<Tool<Payload, Payload>> toolList) {
        for (Tool<Payload, Payload> tool : toolList) {
            tools.put(tool.name(), tool);
        }
    }

    public Tool<Payload, Payload> get(String name) {
        return tools.get(name);
    }

    public String describeTools() {
        StringBuilder sb = new StringBuilder();
        for (Tool<Payload, Payload> tool : tools.values()) {
            sb.append("- ").append(tool.name())
                .append(": ")
                .append(tool.description())
                .append("\n");
        }
        return sb.toString();
    }
}
