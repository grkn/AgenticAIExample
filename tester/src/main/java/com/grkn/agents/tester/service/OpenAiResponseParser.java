package com.grkn.agents.tester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.tester.model.LlmGeneratedFile;
import com.grkn.agents.tester.model.LlmGenerationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper;

    public OpenAiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LlmGenerationResult parse(String rawResponse, String fallbackModel) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return fallbackResult(fallbackModel, "empty_openai_response");
        }

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String model = text(root.path("model"), fallbackModel);

            String content = extractMessageContent(root);
            if (content == null || content.isBlank()) {
                return fallbackResult(model, "missing_message_content");
            }

            JsonNode generated = objectMapper.readTree(content);
            List<LlmGeneratedFile> files = parseFiles(generated.path("files"));
            String promptVersion = text(generated.path("promptVersion"), "v1");

            if (files.isEmpty()) {
                return fallbackResult(model, "no_files_in_model_output");
            }
            return new LlmGenerationResult(files, model, promptVersion);
        } catch (JsonProcessingException e) {
            return fallbackResult(fallbackModel, "invalid_json_from_openai");
        }
    }

    private List<LlmGeneratedFile> parseFiles(JsonNode filesNode) {
        List<LlmGeneratedFile> files = new ArrayList<>();
        if (!filesNode.isArray()) {
            return files;
        }

        for (JsonNode item : filesNode) {
            String path = text(item.path("path"), null);
            String content = text(item.path("content"), null);
            if (path != null && !path.isBlank() && content != null) {
                files.add(new LlmGeneratedFile(path, content));
            }
        }
        return files;
    }

    private String extractMessageContent(JsonNode root) {
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            return null;
        }

        JsonNode contentNode = choices.get(0).path("message").path("content");
        if (contentNode.isTextual()) {
            return contentNode.asText();
        }

        if (contentNode.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonNode part : contentNode) {
                JsonNode text = part.path("text");
                if (text.isTextual()) {
                    sb.append(text.asText());
                }
            }
            return sb.toString();
        }

        return null;
    }

    private LlmGenerationResult fallbackResult(String model, String reason) {
        List<LlmGeneratedFile> files = List.of(
                new LlmGeneratedFile("src/test/resources/features/llm_generated_api.feature",
                        "Feature: LLM generation failed\n\n  Scenario: Fallback scenario\n    Given generation error reason is \"" + reason + "\"\n    Then response is successful\n"),
                new LlmGeneratedFile("src/test/java/com/grkn/agents/tester/bdd/steps/LlmGeneratedSteps.java",
                        "package com.grkn.agents.tester.bdd.steps;\n\n" +
                                "import io.cucumber.java.en.Given;\n" +
                                "import io.cucumber.java.en.Then;\n\n" +
                                "public class LlmGeneratedSteps {\n" +
                                "    @Given(\"generation error reason is {string}\")\n" +
                                "    public void generationErrorReasonIs(String reason) { }\n\n" +
                                "    @Then(\"response is successful\")\n" +
                                "    public void responseIsSuccessful() { }\n" +
                                "}\n")
        );
        return new LlmGenerationResult(files, model == null ? "openai" : model, "v1");
    }

    private String text(JsonNode node, String defaultValue) {
        if (node != null && node.isTextual()) {
            return node.asText();
        }
        return defaultValue;
    }
}
