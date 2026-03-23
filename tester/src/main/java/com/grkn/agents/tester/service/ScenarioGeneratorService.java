package com.grkn.agents.tester.service;

import com.grkn.agents.tester.model.RequirementDoc;
import com.grkn.agents.tester.model.ScenarioModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ScenarioGeneratorService {

    public List<ScenarioModel> generate(List<RequirementDoc> requirements) {
        List<ScenarioModel> scenarios = new ArrayList<>();
        for (RequirementDoc requirement : requirements) {
            if (requirement.acceptanceCriteria() == null || requirement.acceptanceCriteria().isEmpty()) {
                scenarios.add(defaultScenario(requirement));
                continue;
            }

            int index = 1;
            for (String ac : requirement.acceptanceCriteria()) {
                String normalizedAc = sanitize(ac);
                scenarios.add(new ScenarioModel(
                        requirement.requirementId(),
                        sanitize(requirement.title()),
                        "Scenario " + index + " - " + normalizedAc,
                        List.of("the requirement context is prepared"),
                        List.of("the user executes the flow for " + normalizedAc),
                        List.of("the expected outcome is satisfied for " + normalizedAc)
                ));
                index++;
            }
        }
        return scenarios;
    }

    private ScenarioModel defaultScenario(RequirementDoc requirement) {
        return new ScenarioModel(
                requirement.requirementId(),
                sanitize(requirement.title()),
                "Default scenario for " + sanitize(requirement.title()),
                List.of("the requirement is available"),
                List.of("the user performs the main action"),
                List.of("the system returns a successful result")
        );
    }

    private String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return "unnamed";
        }
        return input.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}
