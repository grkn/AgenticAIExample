package com.grkn.agents.architect.resource;

import com.grkn.agents.architect.type.ArchitectureMode;

public record ArchitectureRequest(String requirements, String constraints, String context, ArchitectureMode mode) {
}
