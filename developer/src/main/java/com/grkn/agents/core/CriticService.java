package com.grkn.agents.core;

import org.springframework.stereotype.Service;

@Service
public class CriticService {

    private final LlmClient llmClient;

    public CriticService(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public String evaluate(String goal, String candidateAnswer, String evidence) throws Exception {
        String prompt = """
                You are a strict reviewer of a coding agent run.

                Goal:
                %s

                Candidate answer:
                %s

                Evidence:
                %s

                Return JSON only with this shape:
                {
                  "action": "FINAL_ANSWER",
                  "toolName": "",
                  "toolInput": "",
                  "reasoning": "Brief critique of grounding, missing risks, and confidence",
                  "finalAnswer": "A concise reviewer note"
                }
                """.formatted(goal, candidateAnswer, evidence);

        return llmClient.askForJson(prompt);
    }
}
