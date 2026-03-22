package com.grkn.agents.architect.type;

import com.grkn.agents.architect.core.PromptModeHandler;
import org.springframework.stereotype.Component;

@Component
public class MigrationPromptModeHandler implements PromptModeHandler {

    @Override
    public ArchitectureMode mode() {
        return ArchitectureMode.MIGRATION;
    }

    @Override
    public String outputSections() {
        return "Current State, Target State, Migration Plan, Risks, Rollback Strategy.";
    }
}
