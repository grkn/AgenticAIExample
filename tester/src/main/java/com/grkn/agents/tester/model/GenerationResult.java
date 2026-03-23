package com.grkn.agents.tester.model;

import java.nio.file.Path;
import java.util.List;

public record GenerationResult(
        List<Path> generatedFiles,
        List<ScenarioModel> scenarios
) {
}
