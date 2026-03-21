package com.grkn.agents.core;

import java.util.List;

public interface ProductOwnerAgent {

    String clarifyProblem(String rawProblemStatement) throws Exception;

    List<SubProblem> splitIntoSubProblems(String clarifiedProblem) throws Exception;

    String buildRequirementsDocument(String clarifiedProblem, List<SubProblem> subProblems, List<String> observations);
}
