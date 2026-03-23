package com.grkn.agents.tester.controller;

import com.grkn.agents.tester.model.ExecutionResult;
import com.grkn.agents.tester.model.RunRecord;
import com.grkn.agents.tester.model.RunRequest;
import com.grkn.agents.tester.model.RunStatus;
import com.grkn.agents.tester.service.RunOrchestratorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/runs")
public class RunController {

    private final RunOrchestratorService runOrchestratorService;

    public RunController(RunOrchestratorService runOrchestratorService) {
        this.runOrchestratorService = runOrchestratorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> startRun(@RequestBody RunRequest request) {
        RunRecord run = runOrchestratorService.startRun(request);
        return Map.of(
                "runId", run.runId(),
                "status", run.status().name()
        );
    }

    @GetMapping("/{runId}")
    public RunRecord getRun(@PathVariable String runId) {
        return runOrchestratorService.getRun(runId);
    }

    @GetMapping("/{runId}/results")
    public ExecutionResult getResults(@PathVariable String runId) {
        RunRecord run = runOrchestratorService.getRun(runId);
        return run.executionResult();
    }

    @PostMapping("/{runId}/cancel")
    public Map<String, String> cancel(@PathVariable String runId) {
        RunRecord run = runOrchestratorService.getRun(runId);
        RunRecord updated = new RunRecord(
                run.runId(),
                run.requirementsRoot(),
                run.workspaceRoot(),
                run.tags(),
                RunStatus.CANCELLED,
                run.createdAt(),
                run.updatedAt(),
                run.generationResult(),
                run.executionResult(),
                run.error()
        );
        return Map.of("runId", updated.runId(), "status", updated.status().name());
    }
}
