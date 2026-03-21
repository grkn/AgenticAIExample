# Agents-HITL

A Spring Boot based **multi-agent repository** that demonstrates two practical AI agents:
- **Developer Agent**: plans and executes engineering tasks using tools on a local codebase
- **Product Owner Agent**: decomposes product requests into clear, implementable sub-problems

This project is designed for **Human-in-the-Loop (HITL)** workflows, where agents can work autonomously but still ask for clarification when needed.

---

## What this repository contains

This is a multi-module Maven project:
- **Root module (`agents`)**: aggregator `pom`
- **`developer` module**: implementation of the Developer Agent orchestrator + tool execution
- **`productowner` module**: implementation of the Product Owner Agent for requirement decomposition

Key technologies:
- Java 21
- Spring Boot 3.5.0
- Jackson
- Java `HttpClient` for OpenAI API integration

---

## Developer Agent (developer module)

The Developer Agent is responsible for turning a coding goal into concrete repository actions.

### What it does
1. Understands the engineering goal and repository path
2. Plans the next action with LLM support
3. Executes one tool at a time (LIST, READ, SEARCH, WRITE, REPLACE, MAVEN COMPILE)
4. Feeds tool output back into planning context
5. Repeats until completed or blocked
6. Produces a final answer and run summary

### Main components
- `Application`
  - Boots Spring context and can start an interactive HITL run

- `DeveloperAgentOrchestrator`
  - Core execution loop controller
  - Tracks observations, tool history, and modified files

- `PlannerService`
  - Builds prompt and requests the next strict JSON action

- `LlmClient`
  - Calls OpenAI Responses API

- `CriticService`
  - Performs final quality review

- `tools/*`
  - `ListFile`, `ReadFile`, `SearchPatternInFile`, `WriteFile`, `ReplaceFile`, `RunMavenCompileTool`

### Developer Agent outcomes
`AgentResult.outcome`:
- `COMPLETED`
- `NEEDS_HUMAN_INPUT`
- `STOPPED_MAX_STEPS`

---

## Product Owner Agent (productowner module)

The Product Owner Agent focuses on **requirement analysis**.

### What it does
1. Takes a high-level product request
2. Breaks it into smaller, meaningful sub-problems
3. Returns structured outputs that can be consumed by developers/agents

### Main components
- `ProductOwner`
  - Entry point for running PO workflows

- `ProductOwnerAgent`
  - Agent contract/interface

- `DefaultProductOwnerAgent`
  - Default implementation that performs request decomposition

- `SubProblem`
  - Model representing each decomposed requirement item

- `LlmClient`
  - OpenAI API communication for product reasoning

This module helps translate product intent into actionable work items before development starts.

---

## Project structure

```
Agents-HITL/
  pom.xml
  developer/
    pom.xml
    src/main/java/com/grkn/agents/
      Application.java
      core/
      tools/
      config/
      properties/
    src/main/resources/application.yaml
  productowner/
    pom.xml
    src/main/java/com/grkn/agents/
      ProductOwner.java
      core/
      config/
      properties/
    src/main/resources/application.yaml
```

---

## Configuration

Both modules use `application.yaml` with OpenAI settings:
- `agent.openai.base-url`
- `agent.openai.model`
- `agent.openai.api-key` (from `OPENAI_API_KEY`)

Set API key (PowerShell):

```powershell
$env:OPENAI_API_KEY = "your-api-key"
```

---

## Build and run

From repository root:

```bash
mvn clean compile
```

Run Developer Agent from IDE via `Application.main()` (developer module).
Run Product Owner Agent from IDE via `ProductOwner.main()` (productowner module).

---

## New implementations

- **21.03.2026**: JavaFX UI added and bound to agent actions.
- **21.03.2026**: Product Owner Agent implemented and tested by Developer Agent.

<img width="865" height="792" alt="image" src="https://github.com/user-attachments/assets/b054acc7-e0bd-4945-96d2-768b72b46c42" />

<img width="1278" height="821" alt="image" src="https://github.com/user-attachments/assets/5b212445-fa3a-4660-b427-ac040c43533b" />
