package com.grkn.agents.core;

import org.springframework.stereotype.Component;

@Component
public class Validator {

    public ValidationResult validate(String architectureText) {
        if (architectureText == null || architectureText.isBlank()) {
            return new ValidationResult(false, "Architecture output is empty");
        }

        boolean hasOverview = architectureText.contains("Overview");
        boolean hasComponents = architectureText.contains("Components");

        if (hasOverview && hasComponents) {
            return new ValidationResult(true, "Valid architecture output");
        }

        return new ValidationResult(false, "Missing required sections: Overview/Components");
    }

    public record ValidationResult(boolean valid, String message) {}
}
