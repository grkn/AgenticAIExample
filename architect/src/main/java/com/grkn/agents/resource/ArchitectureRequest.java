package com.grkn.agents.resource;

import com.grkn.agents.type.ArchitectureMode;

public record ArchitectureRequest(String requirements, String constraints, String context, ArchitectureMode mode) {
}
