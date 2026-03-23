package com.grkn.agents.tester.model;

import java.util.List;

public record RequirementDoc(
        String requirementId,
        String title,
        String description,
        List<String> acceptanceCriteria
) {
}
