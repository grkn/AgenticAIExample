# AgenticAIExample

AgenticAIExample is a **multi-module Spring Boot project** that demonstrates how multiple AI-driven agents can collaborate in a software workflow (product definition, architecture, development, testing, and orchestration).

This repository is designed to be practical for developers: each module has a clear responsibility, configurable LLM integration, and runnable entry points.

---

## What this project includes

The root Maven project (`pom.xml`) includes these modules:

- `productowner` – breaks high-level ideas into implementable sub-problems
- `architect` – produces architecture-focused outputs with multiple prompt modes
- `developer` – executes coding tasks using tool-based actions (read/write/replace/search/compile, etc.)
- `tester` – generates and runs test artifacts (including BDD-related flows)
- `orchestrator` – hosts orchestration/UI entry points to tie agent flows together

---

## High-level architecture

The repository follows an **agent-per-responsibility** architecture:

1. **Product Owner Agent**
   - Interprets product intent
   - Produces structured requirement decomposition

2. **Architect Agent**
   - Converts requirements into architectural guidance
   - Supports multiple architecture prompt modes (default/review/migration/interview)

3. **Developer Agent**
   - Performs repo-level engineering tasks with explicit tools
   - Uses iterative planning/critic loops and can request human input

4. **Tester Agent**
   - Generates runnable test artifacts from requirements/endpoints
   - Can execute Maven test flows and store run information

5. **Orchestrator**
   - Provides glue/UI entry point for coordinating modules

This separation keeps concerns clean and makes it easier to evolve each capability independently.

---

## Technology stack

- **Java 21**
- **Spring Boot 3.5.0**
- **Maven (multi-module build)**
- **Jackson / JSON processing**
- **Java HttpClient** for LLM API communication

---

## Repository structure

```text
AgenticAIExample/
├─ pom.xml
├─ README.md
├─ architect/
│  ├─ pom.xml
│  └─ src/main/...
├─ developer/
│  ├─ pom.xml
│  └─ src/main/...
├─ productowner/
│  ├─ pom.xml
│  └─ src/main/...
├─ tester/
│  ├─ pom.xml
│  └─ src/main/...
└─ orchestrator/
   ├─ pom.xml
   └─ src/main/...
```

---

## Module details

### 1) `productowner`
Purpose: convert broad product requests into actionable chunks.

Key concepts found in code:
- `ProductOwnerAgent` abstraction and default implementation
- LLM client + OpenAI properties configuration
- Output model such as `SubProblem`

Typical use case:
- Input: “Build feature X with constraints Y”
- Output: decomposed sub-problems for implementation planning

### 2) `architect`
Purpose: provide architecture-aware analysis and recommendations.

Key concepts found in code:
- Prompt builder and mode handlers
- `ArchitectureMode` and specialized handlers (`Default`, `Review`, `Migration`, `Interview`)
- Request/response models and validation

Typical use case:
- Input: requirements or existing-system context
- Output: architecture guidance based on selected mode

### 3) `developer`
Purpose: execute engineering tasks in a repository through explicit tools.

Key concepts found in code:
- `DeveloperAgentOrchestrator`
- Planning & critique services (`PlannerService`, `CriticService`)
- Tool registry and concrete tools:
  - list files
  - read file
  - write file
  - replace file
  - search pattern
  - delete file
  - run Maven compile

Typical use case:
- Input: concrete task + repo path + operating rules
- Output: iterative tool calls + final status (`COMPLETED`, `NEEDS_HUMAN_INPUT`, etc.)

### 4) `tester`
Purpose: generate and run test assets, including BDD-related workflows.

Key concepts found in code:
- Controllers for generation and execution
- Services for requirement ingestion, endpoint prompt building, LLM generation, file ops, and Maven execution
- Run tracking models (`RunRecord`, `RunStatus`, `ExecutionResult`)

Typical use case:
- Input: requirement document / endpoint metadata
- Output: generated test artifacts and execution reports

### 5) `orchestrator`
Purpose: provide top-level orchestration and UI entry classes.

Key concepts found in code:
- Main launcher
- UI app entry (`AgentUiApp`)

---

## Prerequisites

- JDK **21**
- Maven **3.9+** (recommended)
- Network access for OpenAI-compatible API calls
- OpenAI API key (or compatible provider key, based on your config)

---

## Configuration

Each module includes its own `src/main/resources/application.yaml` for settings.

Common OpenAI-related properties (naming may vary by module):
- `agent.openai.base-url`
- `agent.openai.model`
- `agent.openai.api-key`

### Setting API key (PowerShell)

```powershell
$env:OPENAI_API_KEY = "your-api-key"
```

If your module reads from YAML directly, update that file accordingly. Avoid committing secrets.

---

## Build the project

From repository root:

```bash
mvn clean compile
```

Build a single module if needed:

```bash
mvn -pl developer -am clean compile
```

---

## Run modules

You can run from your IDE (recommended for development) by executing each module's main class.

Examples from current codebase:
- **Product Owner**: `com.grkn.agents.ProductOwner`
- **Architect**: `com.grkn.agents.architect.SoftwareArchitect`
- **Developer**: `com.grkn.agents.Developer`
- **Tester**: `com.grkn.agents.AgentsApplication`
- **Orchestrator**: `com.grkn.agents.orchestartor.Main`

> Note: package name `orchestartor` is used in current source paths.

---

## Developer workflow suggestion

A practical workflow across agents:

1. Start with `productowner` to break down business intent
2. Pass outputs to `architect` for technical direction
3. Execute coding tasks with `developer` tools
4. Validate outcomes using `tester` generation + execution
5. Coordinate end-to-end runs with `orchestrator`

---

## Tips for contributors

- Keep module boundaries clear (avoid leaking responsibilities)
- Prefer explicit tool/action steps for reproducibility
- Add/adjust DTOs in each module's `resource`/`model` packages
- Keep prompts and mode handlers focused and testable
- Run `mvn compile` at root before pushing changes

---

## Troubleshooting

- **Compilation error due to JDK**: verify Java 21 is active
- **LLM call failures**: check API key, base URL, model name, and internet access
- **Unexpected agent output**: review prompt builders/mode handlers and request payload models
- **Module run issues in IDE**: refresh Maven project and confirm module-specific classpath/resources

---

## License

Add your preferred license file (`LICENSE`) if this repository will be shared publicly.
