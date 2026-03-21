package com.grkn.agents.core;

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
