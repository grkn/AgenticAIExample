package com.grkn.agents.architect.type;

import com.grkn.agents.architect.core.PromptModeHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultPromptModeHandler implements PromptModeHandler {

    @Override
    public ArchitectureMode mode() {
        return ArchitectureMode.DEFAULT;
    }

    @Override
    public String outputSections() {
        return "Overview, Components, Data Flow, Risks, Trade-offs.";
    }
}
