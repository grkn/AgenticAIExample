package com.grkn.agents.tester.model;

import java.util.List;

public record EndpointSpec(
        String serviceName,
        String baseUrl,
        List<EndpointInfo> endpoints,
        String authType
) {
}
