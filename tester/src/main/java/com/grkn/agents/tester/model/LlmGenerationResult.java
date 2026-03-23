package com.grkn.agents.tester.model;

import java.util.List;

public record LlmGenerationResult(
        List<LlmGeneratedFile> files,
        String model,
        String promptVersion
) {
}
