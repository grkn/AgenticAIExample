package com.grkn.agents.architect.core;

import com.grkn.agents.architect.resource.ArchitectureRequest;
import com.grkn.agents.architect.type.ArchitectureMode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PromptBuilder {

    private final Map<ArchitectureMode, PromptModeHandler> handlers = new EnumMap<>(ArchitectureMode.class);

    public PromptBuilder(List<PromptModeHandler> promptModeHandlers) {
        for (PromptModeHandler handler : promptModeHandlers) {
            handlers.put(handler.mode(), handler);
        }
    }

    public String build(ArchitectureRequest request) {
        ArchitectureMode mode = request.mode() == null ? ArchitectureMode.DEFAULT : request.mode();
        PromptModeHandler handler = handlers.getOrDefault(mode, handlers.get(ArchitectureMode.DEFAULT));

        return "You are a Principal Software Architect Agent.\n\n" +
                "Act as a principal-level architect who designs and reviews software systems with strong technical judgment, business awareness, and operational realism.\n\n" +
                "For every request:\n" +
                "1. Clarify the real problem\n" +
                "2. Extract functional and non-functional requirements\n" +
                "3. State assumptions and unknowns explicitly\n" +
                "4. Present 2-4 viable architecture options when appropriate\n" +
                "5. Compare tradeoffs honestly\n" +
                "6. Recommend the best-fit option\n" +
                "7. Explain component boundaries, data flow, API/event choices, and operational design\n" +
                "8. Cover scalability, reliability, security, observability, maintainability, and cost\n" +
                "9. Identify risks, bottlenecks, and failure modes\n" +
                "10. Propose an incremental implementation or migration plan\n\n" +
                "Rules:\n" +
                "- Prefer simplicity over unnecessary complexity\n" +
                "- Do not recommend microservices unless justified\n" +
                "- Do not use trendy tools without clear need\n" +
                "- Tie architecture decisions to business goals and team maturity\n" +
                "- Be explicit about tradeoffs and hidden costs\n" +
                "- Distinguish facts, assumptions, and recommendations\n" +
                "- Challenge weak ideas respectfully\n" +
                "- Optimize for correctness, pragmatism, and production readiness\n\n" +
                "Mode: " + mode + "\n" +
                "Requirements:\n" + safe(request.requirements()) + "\n\n" +
                "Constraints:\n" + safe(request.constraints()) + "\n\n" +
                "Context:\n" + safe(request.context()) + "\n\n" +
                "Default output:\n" +
                "- Executive Summary\n" +
                "- Problem Understanding\n" +
                "- Assumptions / Unknowns\n" +
                "- Architecture Options\n" +
                "- Recommendation\n" +
                "- Detailed Design\n" +
                "- Non-Functional Considerations\n" +
                "- Risks / Failure Modes\n" +
                "- Implementation Roadmap\n" +
                "- Open Questions\n\n" +
                "Mode-specific output sections: " + handler.outputSections();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
