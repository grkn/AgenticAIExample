package com.grkn.agents.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.resource.AgentState;
import com.grkn.agents.core.LlmClient;
import com.grkn.agents.resource.PlannerDecision;
import com.grkn.agents.tools.Payload;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    public PlannerService(LlmClient llmClient, ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
    }

    public PlannerDecision decideNextStep(AgentState state, String toolDescriptions, Payload payload) throws Exception {
        String prompt = """
                You are a Senior Java developer agent working on a Spring Boot repository.
                
                Your goal is to complete the requested engineering task using available tools.
                
                Repo path:
                %s
                
                Goal:
                %s
                
                Available tools:
                %s
                
                Basic options to operate:
                
                You can use available tools to operate. Descriptions to operate are below:
                
                - List files: you can search for repository to select a file to learn more about content of selected file
                - Read a file: you can read content of file
                - Replace a file: you can overwrite a file content
                - Write a file: you can create a file and write from scratch inside repository
                - Search pattern inside file: you can search regex inside a file.
                - Maven compile: you can run mvn compile to understand code is compiled or not.
                
                Rules:
                - First inspect the repo before changing files.
                - Second learn content of file or files that you have listed
                - Prefer minimal targeted changes.
                - After changing code, run compile.
                - If compile fails, inspect errors and fix them.
                - Use one tool per step.
                - Return JSON as final result only.
                
                Only Allowed actions:
                - RUN_TOOL
                - ASK_HUMAN
                - FINAL_ANSWER
                
                toolInput and toolOutput format must be Json shape:
                {
                    "content": "current content of file"
                    "filePath": "current selected file's absolute path"
                    "newContent": "new content of selected file that needs to be replaced"
                    "rootPath": "repository's absolute path"
                    "resultOfSearch": "contains result of search operation as file names"
                    "mvnResult": "result of running 'mvn compile' in the repository"
                    "searchPattern": "to search pattern's string value which return absolute paths contains pattern"
                }
                
                Final Result JSON shape:
                {
                  "action": "RUN_TOOL or ASK_HUMAN",
                  "toolName": "tool name or empty string",
                  "toolInput": "tool input which is json as string or empty json object as string",
                  "reasoning": "brief explanation",
                  "finalAnswer": "filled only for FINAL_ANSWER"
                  "toolOutput": "tool's output as json string you need to use: %s"
                }
                
                
                
                """.formatted(
                state.getRepoPath(),
                state.getGoal(),
                toolDescriptions,
                objectMapper.writeValueAsString(payload)
        );

        String json = llmClient.askForJson(prompt);
        return objectMapper.readValue(json, PlannerDecision.class);
    }
}
