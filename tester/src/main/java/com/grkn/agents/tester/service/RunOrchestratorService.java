package com.grkn.agents.tester.service;

import com.grkn.agents.tester.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RunOrchestratorService {

    private final RunStore runStore;
    private final LlmClientService llmClientService;
    private final MavenExecutionService mavenExecutionService;

    public RunOrchestratorService(RunStore runStore, LlmClientService llmClientService, MavenExecutionService mavenExecutionService) {
        this.runStore = runStore;
        this.llmClientService = llmClientService;
        this.mavenExecutionService = mavenExecutionService;
    }

    public RunRecord startRun(RunRequest request) {
        String runId = UUID.randomUUID().toString();
        RunRecord created = new RunRecord(
                runId,
                request.requirementsRoot(),
                request.workspaceRoot(),
                request.tags(),
                RunStatus.PENDING,
                Instant.now(),
                Instant.now(),
                null,
                null,
                null
        );
        runStore.save(created);

        try {
            RunRecord validating = runStore.save(created.withStatus(RunStatus.VALIDATING));

            LlmGenerationResult llmResult = llmClientService.generateBddArtifacts(request.endpointSpec());
            RunRecord generating = runStore.save(validating.withStatus(RunStatus.GENERATING));

            List<Path> generatedFiles = persistGeneratedFiles(request.workspaceRoot(), llmResult.files());
            GenerationResult generationResult = new GenerationResult(generatedFiles, new ArrayList<>());
            RunRecord executing = runStore.save(generating.withGenerationResult(generationResult).withStatus(RunStatus.EXECUTING));

            ExecutionResult executionResult = mavenExecutionService.runBddTests(request.workspaceRoot(), request.tags());
            RunStatus finalStatus = executionResult.exitCode() == 0 ? RunStatus.COMPLETED : RunStatus.FAILED;
            RunRecord aggregated = executing.withExecutionResult(executionResult).withStatus(RunStatus.AGGREGATING);
            return runStore.save(aggregated.withStatus(finalStatus));
        } catch (Exception e) {
            RunRecord failed = runStore.findById(runId).orElse(created).withError(e.getMessage());
            return runStore.save(failed);
        }
    }

    public RunRecord getRun(String runId) {
        return runStore.findById(runId).orElseThrow(() -> new IllegalArgumentException("Run not found: " + runId));
    }

    private List<Path> persistGeneratedFiles(String workspaceRoot, List<LlmGeneratedFile> files) throws IOException {
        Path root = Paths.get(workspaceRoot).toAbsolutePath().normalize();
        List<Path> created = new ArrayList<>();
        if (files == null) {
            return created;
        }
        for (LlmGeneratedFile file : files) {
            Path target = root.resolve(file.path()).normalize();
            if (!target.startsWith(root)) {
                throw new IllegalArgumentException("Unsafe output path from llm: " + file.path());
            }
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.writeString(target, file.content() == null ? "" : file.content());
            created.add(target);
        }
        return created;
    }
}
