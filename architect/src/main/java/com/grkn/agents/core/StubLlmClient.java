package com.grkn.agents.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(LlmClient.class)
public class StubLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        return "Overview:\nProposed architecture generated from prompt.\n\n" +
                "Components:\n- API Layer\n- Service Layer\n- Persistence Layer\n\n" +
                "Data Flow:\nClient -> API -> Service -> Database\n\n" +
                "Risks:\n- Scalability assumptions may need validation\n\n" +
                "Trade-offs:\n- Simplicity over advanced optimization\n\n" +
                "Prompt Snapshot:\n" + prompt;
    }
}
