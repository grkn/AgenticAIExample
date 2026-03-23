package com.grkn.agents.tester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.tester.model.EndpointSpec;
import com.grkn.agents.tester.model.LlmGenerationResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LlmClientService {

    private final OpenAiProperties openAiProperties;
    private final OpenAiResponseParser openAiResponseParser;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public LlmClientService(OpenAiProperties openAiProperties,
                            OpenAiResponseParser openAiResponseParser,
                            ObjectMapper objectMapper) {
        this.openAiProperties = openAiProperties;
        this.openAiResponseParser = openAiResponseParser;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(openAiProperties.connectTimeout())
                .build();
    }

    public LlmGenerationResult generateBddArtifacts(EndpointSpec endpointSpec) {
        String requestBody = buildRequestBody(endpointSpec);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAiProperties.baseUrl() + "/v1/chat/completions"))
                .timeout(openAiProperties.requestTimeout())
                .header("Authorization", "Bearer " + openAiProperties.apiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return openAiResponseParser.parse(response.body(), openAiProperties.model());
            }
            return openAiResponseParser.parse("", openAiProperties.model());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return openAiResponseParser.parse("", openAiProperties.model());
        }
    }

    private String buildRequestBody(EndpointSpec endpointSpec) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", openAiProperties.model());

        String prompt = "Generate BDD artifacts as JSON with fields files[{path,content}] and promptVersion for endpoint spec: " + endpointSpec;
        payload.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are a senior QA engineer generating BDD artifacts."),
                Map.of("role", "user", "content", prompt)
        });

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to build OpenAI request", e);
        }
    }
}
