package com.grkn.agents.core;

import org.springframework.stereotype.Component;

@Component
public class ArchitectAgent {

    private final ArchitectAgentService architectAgentService;

    public ArchitectAgent(ArchitectAgentService architectAgentService) {
        this.architectAgentService = architectAgentService;
    }

    public ArchitectureResponse run(ArchitectureRequest request) {
        return architectAgentService.generateArchitecture(request);
    }
}
