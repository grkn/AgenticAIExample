package com.grkn.agents.tester.model;

public record ExecutionResult(
        int exitCode,
        String command,
        String output
) {
}
