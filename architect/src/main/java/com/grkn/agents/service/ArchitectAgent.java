package com.grkn.agents.service;

import com.grkn.agents.resource.ArchitectureRequest;
import com.grkn.agents.resource.ArchitectureResponse;
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
