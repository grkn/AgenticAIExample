package com.grkn.agents.tester.service;

import com.grkn.agents.tester.model.GenerationResult;
import com.grkn.agents.tester.model.ScenarioModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class BddArtifactService {

    public GenerationResult generateArtifacts(String workspaceRoot, List<ScenarioModel> scenarios) throws IOException {
        Path root = Paths.get(workspaceRoot).toAbsolutePath().normalize();
        Path featureDir = root.resolve("src/test/resources/features");
        Path stepDir = root.resolve("src/test/java/com/grkn/agents/tester/bdd/steps");
        Path runnerDir = root.resolve("src/test/java/com/grkn/agents/tester/bdd");

        Files.createDirectories(featureDir);
        Files.createDirectories(stepDir);
        Files.createDirectories(runnerDir);

        List<Path> generated = new ArrayList<>();

        for (ScenarioModel scenario : scenarios) {
            String safeName = toSafeFileName(scenario.featureName() + "_" + scenario.scenarioName());
            Path featureFile = featureDir.resolve(safeName + ".feature");
            Files.writeString(featureFile, toFeatureContent(scenario));
            generated.add(featureFile);
        }

        Path stepFile = stepDir.resolve("GeneratedSteps.java");
        Files.writeString(stepFile, generatedStepsContent());
        generated.add(stepFile);

        Path runnerFile = runnerDir.resolve("RunCucumberTest.java");
        Files.writeString(runnerFile, runnerContent());
        generated.add(runnerFile);

        return new GenerationResult(generated, scenarios);
    }

    private String toFeatureContent(ScenarioModel scenario) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature: " ).append(capitalize(scenario.featureName())).append("\n\n");
        sb.append("  @generated @e2e\n");
        sb.append("  Scenario: " ).append(capitalize(scenario.scenarioName())).append("\n");
        scenario.givenSteps().forEach(g -> sb.append("    Given " ).append(g).append("\n"));
        scenario.whenSteps().forEach(w -> sb.append("    When " ).append(w).append("\n"));
        scenario.thenSteps().forEach(t -> sb.append("    Then " ).append(t).append("\n"));
        return sb.toString();
    }

    private String generatedStepsContent() {
        return "package com.grkn.agents.tester.bdd.steps;\n\n" +
                "import io.cucumber.java.en.Given;\n" +
                "import io.cucumber.java.en.Then;\n" +
                "import io.cucumber.java.en.When;\n\n" +
                "public class GeneratedSteps {\n\n" +
                "    @Given(\"the requirement context is prepared\")\n" +
                "    public void givenContextPrepared() { }\n\n" +
                "    @Given(\"the requirement is available\")\n" +
                "    public void givenRequirementAvailable() { }\n\n" +
                "    @When(\"the user performs the main action\")\n" +
                "    public void whenMainAction() { }\n\n" +
                "    @When(\"the user executes the flow for {word}\")\n" +
                "    public void whenFlowExecuted(String flow) { }\n\n" +
                "    @Then(\"the system returns a successful result\")\n" +
                "    public void thenSuccessResult() { }\n\n" +
                "    @Then(\"the expected outcome is satisfied for {word}\")\n" +
                "    public void thenExpectedOutcome(String flow) { }\n" +
                "}\n";
    }

    private String runnerContent() {
        return "package com.grkn.agents.tester.bdd;\n\n" +
                "import org.junit.platform.suite.api.ConfigurationParameter;\n" +
                "import org.junit.platform.suite.api.IncludeEngines;\n" +
                "import org.junit.platform.suite.api.SelectClasspathResource;\n" +
                "import org.junit.platform.suite.api.Suite;\n\n" +
                "import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;\n\n" +
                "@Suite\n" +
                "@IncludeEngines(\"cucumber\")\n" +
                "@SelectClasspathResource(\"features\")\n" +
                "@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = \"com.grkn.agents.tester.bdd.steps\")\n" +
                "public class RunCucumberTest { }\n";
    }

    private String toSafeFileName(String input) {
        return input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("(^_+|_+$)", "");
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) return "Unnamed";
        String trimmed = value.trim();
        return Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
    }
}
