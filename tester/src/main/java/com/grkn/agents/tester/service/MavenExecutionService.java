package com.grkn.agents.tester.service;

import com.grkn.agents.tester.model.ExecutionResult;
import org.springframework.stereotype.Service;

@Service
public class MavenExecutionService {

    public ExecutionResult runBddTests(String workspaceRoot, String tags) {
        String command = "mvn -q test" + (tags == null || tags.isBlank() ? "" : " -Dcucumber.filter.tags=" + tags);
        String output = "Simulated BDD execution for workspace=" + workspaceRoot;
        return new ExecutionResult(0, command, output);
    }
}
