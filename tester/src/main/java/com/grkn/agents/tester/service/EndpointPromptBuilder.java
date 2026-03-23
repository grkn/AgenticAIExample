package com.grkn.agents.tester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.tester.model.EndpointInfo;
import com.grkn.agents.tester.model.EndpointSpec;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointPromptBuilder {

    private final ObjectMapper objectMapper;

    public EndpointPromptBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String buildPrompt(EndpointSpec endpointSpec) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a senior QA automation engineer. Generate BDD test artifacts from API metadata.\n");
        prompt.append("Return ONLY valid JSON with exact schema:\n");
        prompt.append("{\"files\":[{\"path\":\"...\",\"content\":\"...\"}],\"model\":\"openai\",\"promptVersion\":\"v1\"}\n");
        prompt.append("Include one .feature file and one Java step definition file.\n");

        prompt.append("Service Name: " ).append(nullSafe(endpointSpec.serviceName())).append("\n");
        prompt.append("Base URL: " ).append(nullSafe(endpointSpec.baseUrl())).append("\n");
        prompt.append("Auth Type: " ).append(nullSafe(endpointSpec.authType())).append("\n");

        List<EndpointInfo> endpoints = endpointSpec.endpoints();
        if (endpoints == null || endpoints.isEmpty()) {
            prompt.append("Endpoints: []\n");
        } else {
            prompt.append("Endpoints:\n");
            for (EndpointInfo endpoint : endpoints) {
                prompt.append("- method: " ).append(nullSafe(endpoint.method())).append("\n");
                prompt.append("  path: " ).append(nullSafe(endpoint.path())).append("\n");
                prompt.append("  requestSchema: " ).append(nullSafe(endpoint.requestSchema())).append("\n");
                prompt.append("  responseSchema: " ).append(nullSafe(endpoint.responseSchema())).append("\n");
            }
        }

        try {
            prompt.append("Raw endpointSpec JSON: " ).append(objectMapper.writeValueAsString(endpointSpec)).append("\n");
        } catch (JsonProcessingException e) {
            prompt.append("Raw endpointSpec JSON unavailable due to serialization error.\n");
        }

        return prompt.toString();
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
