package com.grkn.agents.architect.service;

import com.grkn.agents.architect.resource.ArchitectureRequest;
import com.grkn.agents.architect.resource.ArchitectureResponse;
import org.springframework.stereotype.Component;

@Component
public class ArchitectAgent {

    private final ArchitectAgentService architectAgentService;

    public ArchitectAgent(ArchitectAgentService architectAgentService) {
        this.architectAgentService = architectAgentService;
    }

    public ArchitectureResponse run(ArchitectureRequest request) throws Exception {
        return architectAgentService.generateArchitecture(request);
    }
}
