package com.grkn.agents.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProductOwnerAgent implements ProductOwnerAgent {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    public DefaultProductOwnerAgent(LlmClient llmClient, ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String clarifyProblem(String rawProblemStatement) throws Exception {
        String prompt = "You are a senior product owner. Clarify the business problem below in a concise and actionable way. " +
                "Return plain text only.\n\nBusiness problem:\n" + rawProblemStatement;
        return llmClient.askForJson(prompt).trim();
    }

    @Override
    public List<SubProblem> splitIntoSubProblems(String clarifiedProblem) throws Exception {
        String prompt = "You are a senior product owner working with engineering teams. " +
                "Break the clarified problem into 3-7 technical sub-problems. " +
                "Each sub-problem must include title and description. " +
                "Return ONLY valid JSON array with objects using keys: title, description.\n\n" +
                """
                 Format must be json array:
                 [{
                    "title": "title of the subproblem"
                    "description": "description of the subproblem"
                 }]
                """
                +"Clarified problem:\n" + clarifiedProblem;

        String json = llmClient.askForJson(prompt);
        return objectMapper.readValue(json, new TypeReference<List<SubProblem>>() {});
    }

    @Override
    public String buildRequirementsDocument(String clarifiedProblem, List<SubProblem> subProblems, List<String> observations) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Product Owner Requirements\n\n");
        sb.append("## Clarified Problem\n").append(clarifiedProblem).append("\n\n");

        sb.append("## Requirements with Acceptance Criteria\n");
        for (int i = 0; i < subProblems.size(); i++) {
            SubProblem subProblem = subProblems.get(i);
            sb.append(i + 1).append(". Requirement: " ).append(subProblem.getTitle()).append("\n");
            sb.append("   - Description: " ).append(subProblem.getDescription()).append("\n");
            sb.append("   - Acceptance Criteria:\n");
            sb.append("     - The implementation addresses \"").append(subProblem.getTitle()).append("\".\n");
            sb.append("     - The behavior described in the requirement can be validated through tests or verification steps.\n");
            sb.append("     - The solution is integrated without breaking existing functionality.\n");
        }

        if (observations != null && !observations.isEmpty()) {
            sb.append("\n## Observations\n");
            for (String observation : observations) {
                sb.append("- " ).append(observation).append("\n");
            }
        }

        return sb.toString();
    }
}
