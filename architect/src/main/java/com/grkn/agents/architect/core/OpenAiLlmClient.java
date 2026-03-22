package com.grkn.agents.architect.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.architect.properties.OpenAiProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenAiLlmClient implements LlmClient {

    private final OpenAiProperties openAiProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private String responseId;

    public OpenAiLlmClient(OpenAiProperties openAiProperties, ObjectMapper objectMapper) {
        this.openAiProperties = openAiProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public String generate(String prompt) throws Exception {
        if (openAiProperties.getApiKey() == null || openAiProperties.getApiKey().isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is not configured.");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", openAiProperties.getModel());
        payload.put("input", prompt);
        if (responseId != null) {
            payload.put("previous_response_id", responseId);
        }

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAiProperties.getBaseUrl()))
                .timeout(Duration.ofSeconds(120))
                .header("Authorization", "Bearer " + openAiProperties.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IllegalStateException("LLM call failed: HTTP " + response.statusCode() + " -> " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        responseId = root.path("id").asText();

        JsonNode output = root.path("output");
        if (!output.isArray() || output.isEmpty()) {
            throw new IllegalStateException("LLM response missing output: " + response.body());
        }

        return output.get(output.size() - 1).path("content").get(0).path("text").asText();
    }
}
