package com.grkn.agents.type;

import com.grkn.agents.core.PromptModeHandler;
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
