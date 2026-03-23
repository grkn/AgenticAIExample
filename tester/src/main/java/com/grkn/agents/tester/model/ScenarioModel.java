package com.grkn.agents.tester.model;

import java.util.List;

public record ScenarioModel(
        String requirementId,
        String featureName,
        String scenarioName,
        List<String> givenSteps,
        List<String> whenSteps,
        List<String> thenSteps
) {
}
