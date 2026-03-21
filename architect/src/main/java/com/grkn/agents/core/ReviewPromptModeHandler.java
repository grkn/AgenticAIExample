package com.grkn.agents.core;

import org.springframework.stereotype.Component;

@Component
public class ReviewPromptModeHandler implements PromptModeHandler {

    @Override
    public ArchitectureMode mode() {
        return ArchitectureMode.REVIEW;
    }

    @Override
    public String outputSections() {
        return "Current Architecture Assessment, Strengths, Weaknesses, Risks, Recommendations.";
    }
}
