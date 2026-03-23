package com.grkn.agents.tester.model;

public record EndpointInfo(
        String method,
        String path,
        String requestSchema,
        String responseSchema
) {
}
