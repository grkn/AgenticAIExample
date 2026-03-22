package com.grkn.agents.developer.resource;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class AgentResult {
    private String outcome;
    private String finalAnswer;
    private List<String> modifiedFiles = new ArrayList<>();
    private List<String> observations = new ArrayList<>();

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public List<String> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(List<String> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public List<String> getObservations() {
        return observations;
    }

    public void setObservations(List<String> observations) {
        this.observations = observations;
    }
}
