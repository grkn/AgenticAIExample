package com.grkn.agents.tester.model;

import java.time.Instant;

public record RunRecord(
        String runId,
        String requirementsRoot,
        String workspaceRoot,
        String tags,
        RunStatus status,
        Instant createdAt,
        Instant updatedAt,
        GenerationResult generationResult,
        ExecutionResult executionResult,
        String error
) {

    public RunRecord withStatus(RunStatus newStatus) {
        return new RunRecord(runId, requirementsRoot, workspaceRoot, tags, newStatus, createdAt, Instant.now(), generationResult, executionResult, error);
    }

    public RunRecord withGenerationResult(GenerationResult generationResult) {
        return new RunRecord(runId, requirementsRoot, workspaceRoot, tags, status, createdAt, Instant.now(), generationResult, executionResult, error);
    }

    public RunRecord withExecutionResult(ExecutionResult executionResult) {
        return new RunRecord(runId, requirementsRoot, workspaceRoot, tags, status, createdAt, Instant.now(), generationResult, executionResult, error);
    }

    public RunRecord withError(String error) {
        return new RunRecord(runId, requirementsRoot, workspaceRoot, tags, RunStatus.FAILED, createdAt, Instant.now(), generationResult, executionResult, error);
    }
}
