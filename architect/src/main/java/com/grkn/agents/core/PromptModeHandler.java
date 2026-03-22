package com.grkn.agents.core;

import com.grkn.agents.type.ArchitectureMode;

public interface PromptModeHandler {

    ArchitectureMode mode();

    String outputSections();
}
