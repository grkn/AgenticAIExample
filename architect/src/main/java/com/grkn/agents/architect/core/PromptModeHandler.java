package com.grkn.agents.architect.core;

import com.grkn.agents.architect.type.ArchitectureMode;

public interface PromptModeHandler {

    ArchitectureMode mode();

    String outputSections();
}
