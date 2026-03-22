# Agentic AI Example

This repository is a **Spring Boot multi-module project** that demonstrates AI agent workflows in a practical and readable way.

It currently includes:
- **Developer Agent**: helps perform engineering tasks in a code repository using tools
- **Product Owner Agent**: turns high-level product ideas into clear, actionable sub-problems
- **Architect Agent**: supports architecture-related analysis and prompt modes

The project is built for **Human-in-the-Loop (HITL)** usage: agents can work step-by-step and ask for human input when needed.

---

## Project modules

At the root, this is a Maven aggregator project (`pom.xml`) with these modules:

- `developer`
  - Orchestrates coding tasks
  - Uses tools like list/read/write/replace/search/compile

- `productowner`
  - Decomposes product requirements
  - Produces structured requirement breakdowns

- `architect`
  - Handles architecture-oriented prompting and responses
  - Supports different architecture prompt modes

---

## Tech stack

- **Java 21**
- **Spring Boot 3.5.0**
- **Maven**
- **Jackson**
- Java `HttpClient` (for OpenAI API communication)

---

## How the Developer Agent works (simple view)

1. Reads a task goal and repository path
2. Chooses the next action
3. Runs one tool at a time (LIST, READ, SEARCH, WRITE, REPLACE, MAVEN COMPILE)
4. Reviews tool output
5. Repeats until done or blocked

Possible outcomes:
- `COMPLETED`
- `NEEDS_HUMAN_INPUT`
- `STOPPED_MAX_STEPS`

---

## How the Product Owner Agent works (simple view)

1. Takes a product/problem statement
2. Breaks it into smaller, meaningful work items
3. Returns structured sub-problems that development can implement

---

## Basic folder layout

```
AgenticAIExample/
  pom.xml
  README.md
  architect/
    pom.xml
    src/main/java/...
    src/main/resources/application.yaml
  developer/
    pom.xml
    src/main/java/...
    src/main/resources/application.yaml
  productowner/
    pom.xml
    src/main/java/...
    src/main/resources/application.yaml
```

---

## Configuration

Each module has an `application.yaml` with OpenAI-related settings such as:
- `agent.openai.base-url`
- `agent.openai.model`
- `agent.openai.api-key`

Set API key (PowerShell):

```powershell
$env:OPENAI_API_KEY = "your-api-key"
```

---

## Build

From repository root:

```bash
mvn clean compile
```

---

## Run

Run main classes from your IDE:
- Developer Agent: `developer -> Application.main()`
- Product Owner Agent: `productowner -> ProductOwner.main()`
- Architect Agent: `architect -> SoftwareArchitect.main()` (if configured in your environment)

---

## Recent updates

- JavaFX UI support added and connected with agent actions
- Product Owner Agent implemented and tested by Developer Agent
- Software Architect implemented and tested by Developer Agent

<img width="865" height="792" alt="image" src="https://github.com/user-attachments/assets/b054acc7-e0bd-4945-96d2-768b72b46c42" />
