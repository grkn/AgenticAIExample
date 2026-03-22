package com.grkn.agents.architect.resource;

public record ArchitectureResponse(String architecture, String prompt, boolean valid, String validationMessage) {
}
