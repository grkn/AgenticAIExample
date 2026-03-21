package com.grkn.agents.core;

import org.springframework.stereotype.Component;

@Component
public class InterviewPromptModeHandler implements PromptModeHandler {

    @Override
    public ArchitectureMode mode() {
        return ArchitectureMode.INTERVIEW;
    }

    @Override
    public String outputSections() {
        return "Candidate Approach, Architecture Proposal, Design Decisions, Trade-offs, Follow-up Questions.";
    }
}
