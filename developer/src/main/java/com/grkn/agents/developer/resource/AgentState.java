package com.grkn.agents.developer.resource;

import java.util.ArrayList;
import java.util.List;

public class AgentState {
    private String goal;
    private String repoPath;
    private List<AgentTask> tasks = new ArrayList<>();
    private List<String> observations = new ArrayList<>();
    private List<String> toolHistory = new ArrayList<>();
    private List<String> modifiedFiles = new ArrayList<>();
    private String finalAnswer;
    private boolean finished;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public List<AgentTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<AgentTask> tasks) {
        this.tasks = tasks;
    }

    public List<String> getObservations() {
        return observations;
    }

    public void setObservations(List<String> observations) {
        this.observations = observations;
    }

    public List<String> getToolHistory() {
        return toolHistory;
    }

    public void setToolHistory(List<String> toolHistory) {
        this.toolHistory = toolHistory;
    }

    public List<String> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(List<String> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
