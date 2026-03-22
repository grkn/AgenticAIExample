package com.grkn.agents.type;

import com.grkn.agents.core.PromptModeHandler;
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
