package com.grkn.agents.tester.controller;

import com.grkn.agents.tester.model.RunRecord;
import com.grkn.agents.tester.model.RunRequest;
import com.grkn.agents.tester.service.RunOrchestratorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bdd-tests")
public class BddGenerationController {

    private final RunOrchestratorService orchestratorService;

    public BddGenerationController(RunOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> generate(@RequestBody RunRequest request) {
        RunRecord record = orchestratorService.startRun(request);
        return Map.of(
                "id", record.runId(),
                "status", record.status().name()
        );
    }

    @GetMapping("/{id}")
    public RunRecord getStatus(@PathVariable String id) {
        return orchestratorService.getRun(id);
    }
}
