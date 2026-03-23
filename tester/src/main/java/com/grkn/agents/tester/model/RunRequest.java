package com.grkn.agents.tester.model;

public record RunRequest(
        String requirementsRoot,
        String workspaceRoot,
        String tags,
        EndpointSpec endpointSpec
) {
}
