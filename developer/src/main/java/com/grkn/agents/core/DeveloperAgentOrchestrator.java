package com.grkn.agents.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.resource.AgentResult;
import com.grkn.agents.resource.AgentState;
import com.grkn.agents.resource.PlannerDecision;
import com.grkn.agents.service.CriticService;
import com.grkn.agents.service.PlannerService;
import com.grkn.agents.tools.Payload;
import com.grkn.agents.tools.Tool;
import com.grkn.agents.tools.ToolRegistry;
import org.springframework.stereotype.Service;

@Service
public class DeveloperAgentOrchestrator {

    private final PlannerService plannerService;
    private final CriticService criticService;
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;

    public DeveloperAgentOrchestrator(
            PlannerService plannerService,
            CriticService criticService,
            ToolRegistry toolRegistry, ObjectMapper objectMapper
    ) {
        this.plannerService = plannerService;
        this.criticService = criticService;
        this.toolRegistry = toolRegistry;
        this.objectMapper = objectMapper;
    }

    public AgentResult run(String repoPath, String goal) throws Exception {
        AgentState state = new AgentState();
        state.setRepoPath(repoPath);
        state.setGoal(goal);

        int maxSteps = 50;
        Payload payload = new Payload();
        for (int i = 0; i < maxSteps && !state.isFinished(); i++) {
            PlannerDecision decision = plannerService.decideNextStep(state, toolRegistry.describeTools(), payload);
            String action = decision.getAction();

            if ("RUN_TOOL".equals(action)) {
                handleToolRun(state, decision);
                payload = objectMapper.readValue(decision.getToolOutput(), Payload.class);
            } else if ("FINAL_ANSWER".equals(action)) {
                return finalizeResult(state, decision.getFinalAnswer());
            } else if ("ASK_HUMAN".equals(action)) {
                state.setFinished(true);
                AgentResult result = new AgentResult();
                result.setOutcome("NEEDS_HUMAN_INPUT");
                result.setFinalAnswer(decision.getReasoning());
                result.setModifiedFiles(state.getModifiedFiles());
                result.setObservations(state.getObservations());
                return result;
            } else {
                state.getObservations().add("Unknown planner action: " + action);
            }
        }

        state.setFinished(true);
        AgentResult result = new AgentResult();
        result.setOutcome("STOPPED_MAX_STEPS");
        result.setFinalAnswer("Stopped after max steps.");
        result.setModifiedFiles(state.getModifiedFiles());
        result.setObservations(state.getObservations());
        return result;

    }

    private void handleToolRun(AgentState state, PlannerDecision decision) throws Exception {
        Tool<Payload, Payload> tool = toolRegistry.get(decision.getToolName());
        if (tool == null) {
            state.getObservations().add("Unknown tool requested: " + decision.getToolName());
            return;
        }
        Payload toolInput;
        try {
            toolInput = objectMapper.readValue(decision.getToolInput(), Payload.class);
        } catch (Exception e) {
            System.out.println("Input can not be parsed to Payload: " + decision.getToolInput());
            throw new RuntimeException(e);
        }
        Payload result = tool.execute(toolInput);

        decision.setToolOutput(objectMapper.writeValueAsString(result));

        state.getToolHistory().add("TOOL=" + tool.name() + "\nINPUT=" + toolInput + "\nOUTPUT=" + decision.getToolOutput());
        state.getObservations().add("RESULT FROM " + tool.name() + ":\n" + result);

        if ("WRITE".equals(tool.name()) || "REPLACE".equals(tool.name())) {
            String modifiedPath = result.getFilePath();
            state.getModifiedFiles().add(modifiedPath);
        }
    }

    private AgentResult finalizeResult(AgentState state, String finalAnswer) throws Exception {
        state.setFinalAnswer(finalAnswer);
        state.setFinished(true);

        String criticReview = criticService.evaluate(
                state.getGoal(),
                finalAnswer,
                String.join("\n\n", state.getObservations())
        );
        state.getObservations().add("CRITIC_REVIEW:\n" + criticReview);

        AgentResult result = new AgentResult();
        result.setOutcome("COMPLETED");
        result.setFinalAnswer(finalAnswer);
        result.setModifiedFiles(state.getModifiedFiles());
        result.setObservations(state.getObservations());
        return result;
    }
}
