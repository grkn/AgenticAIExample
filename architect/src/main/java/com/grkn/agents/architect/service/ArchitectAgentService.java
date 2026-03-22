package com.grkn.agents.architect.service;

import com.grkn.agents.architect.core.LlmClient;
import com.grkn.agents.architect.core.PromptBuilder;
import com.grkn.agents.architect.validation.Validator;
import com.grkn.agents.architect.resource.ArchitectureRequest;
import com.grkn.agents.architect.resource.ArchitectureResponse;
import org.springframework.stereotype.Service;

@Service
public class ArchitectAgentService {

    private final PromptBuilder promptBuilder;
    private final LlmClient llmClient;
    private final Validator validator;

    public ArchitectAgentService(PromptBuilder promptBuilder, LlmClient llmClient, Validator validator) {
        this.promptBuilder = promptBuilder;
        this.llmClient = llmClient;
        this.validator = validator;
    }

    public ArchitectureResponse generateArchitecture(ArchitectureRequest request) throws Exception {
        String prompt = promptBuilder.build(request);
        String architecture = llmClient.generate(prompt);
        Validator.ValidationResult validationResult = validator.validate(architecture);

        return new ArchitectureResponse(
                architecture,
                prompt,
                validationResult.valid(),
                validationResult.message()
        );
    }
}
