# Agents-HITL

A Spring Boot based **Developer Agent Orchestrator** that uses an LLM to iteratively plan and execute engineering tasks over a local repository using a constrained toolset (LIST, READ, SEARCH, WRITE, REPLACE, MAVEN COMPILE).

This project demonstrates a practical **Human-in-the-Loop (HITL)** software agent flow where the model can:
- inspect a codebase,
- make targeted file changes,
- validate via compilation,
- ask for human input when blocked.

---

## What this repository contains

This is a multi-module Maven project:
- **Root module (`agents`)**: aggregator `pom`
- **`developer` module**: main implementation of the planning/execution agent

Key technologies:
- Java 21
- Spring Boot 3.5.0
- Jackson
- Java `HttpClient` for OpenAI API integration

---

## High-level architecture

Core flow is implemented in `DeveloperAgentOrchestrator`:

1. Build initial `AgentState` with repository path + task goal.
2. Ask `PlannerService` for the **next JSON action**.
3. If action is `RUN_TOOL`, execute a registered tool from `ToolRegistry`.
4. Feed tool output back into planner context (payload chaining).
5. Repeat until one of:
   - `FINAL_ANSWER` (completed),
   - `ASK_HUMAN` (needs input),
   - max steps reached.
6. On completion, run `CriticService` for a final self-review.

### Main components

- `Application`
  - Bootstraps Spring context and starts a sample run from `main`.
  - Includes simple terminal loop for HITL when outcome is `NEEDS_HUMAN_INPUT`.

- `DeveloperAgentOrchestrator`
  - Execution loop controller.
  - Maintains tool history, observations, modified files, and final result.

- `PlannerService`
  - Builds the prompt containing task, repository path, tool descriptions, constraints, and current payload.
  - Calls `LlmClient` and maps model output into `PlannerDecision`.

- `LlmClient`
  - Calls OpenAI Responses API.
  - Sends prompt as `input`, keeps `previous_response_id` to continue conversation across steps.

- `CriticService`
  - Performs final quality review of the generated answer and observations.

- Tool implementations (`com.grkn.agents.tools`)
  - `ListFile`
  - `ReadFile`
  - `SearchPatternInFile`
  - `WriteFile`
  - `ReplaceFile`
  - `RunMavenCompileTool`

---

## Project structure

```
Agents-HITL/
  pom.xml                     # Root aggregator pom
  developer/
    pom.xml                   # Spring Boot module
    src/main/java/com/grkn/agents/
      Application.java
      config/
      core/                   # Orchestration + planner + critic + models
      properties/             # @ConfigurationProperties classes
      tools/                  # Tool contracts + implementations
    src/main/resources/
      application.yaml
```

---

## Configuration

`developer/src/main/resources/application.yaml` contains:

- `agent.openai.base-url` (default: `https://api.openai.com/v1/responses`)
- `agent.openai.model` (currently `gpt-5.3-codex`)
- `agent.openai.api-key` from `OPENAI_API_KEY` env var

> Important: Use environment variable `OPENAI_API_KEY` in real usage.

Example (PowerShell):

```powershell
$env:OPENAI_API_KEY = "your-api-key"
```

---

## Build and run

From repository root:

```bash
mvn clean compile
```

Run application (module `developer`):

```bash
mvn -pl developer exec:java -Dexec.mainClass=com.grkn.agents.Application
```

(Or run `Application.main()` directly from IDE.)

---

## How the agent decides actions

Planner outputs strict JSON with these fields:
- `action` (`RUN_TOOL` or `ASK_HUMAN`)
- `toolName`
- `toolInput` (JSON string)
- `reasoning`
- `finalAnswer`
- `toolOutput` (JSON string from previous step)

The orchestrator then:
- deserializes `toolInput` into `Payload`,
- executes corresponding tool,
- serializes output back into planner memory for next step.

This creates a controlled, auditable agent loop.

---

## Notes for developers

1. **Security**
   - Never commit real API keys.
   - Ensure `application.yaml` uses env interpolation only.

2. **Paths in `Application`**
   - Current `main` contains a hard-coded Windows path for local testing.
   - Consider externalizing it (args/config) for portability.

3. **Prompt contract is critical**
   - `PlannerService` expects strict JSON schema from the model.
   - Any schema drift can break deserialization into `PlannerDecision`.

4. **Tooling extension**
   - Add new tool implementation under `tools/` and register it in `ToolRegistry`.
   - Keep tool I/O aligned with `Payload`.

---

## Typical execution outcomes

`AgentResult.outcome` can be:
- `COMPLETED`
- `NEEDS_HUMAN_INPUT`
- `STOPPED_MAX_STEPS`

Alongside:
- `finalAnswer`
- `modifiedFiles`
- `observations`

---

## Why this project is useful

This repository is a compact reference implementation for building a **tool-using coding agent** in Java/Spring with:
- deterministic tool boundaries,
- iterative planning,
- optional human intervention,
- post-run critique and traceability.


## New Implementations with DateTime

- 21.03.2026: JavaFX is implemented by itself

<img width="865" height="792" alt="image" src="https://github.com/user-attachments/assets/b054acc7-e0bd-4945-96d2-768b72b46c42" />
