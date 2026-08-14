# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

WebProtégé is a collaborative OWL 2 ontology editor that runs as a Java web application. **Note:** This repository is in maintenance mode; active development has moved to a microservice architecture at [webprotege-next-gen](https://github.com/protegeproject/webprotege-next-gen/wiki).

## Build Commands

```bash
# Full build (GWT compilation is slow — ~10-15 min)
mvn clean package

# Build without GWT (server-side only, much faster)
mvn clean package -P skip-gwt-compilation

# Run all tests
mvn test

# Run tests for a specific module
mvn test -pl webprotege-server-core

# Run a single test class within a module
mvn test -pl webprotege-server-core -Dtest=SomeClass_TestCase

# Development mode: start GWT code server (terminal 1)
mvn gwt:codeserver

# Development mode: start Tomcat (terminal 2)
mvn -Denv=dev tomcat7:run
# Then browse to http://localhost:8080
```

## Docker

```bash
docker-compose up -d
# Create admin account after startup:
docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
docker-compose down
```

Data persists in `./.protegedata/` (MongoDB at `mongodb/`, app data at `protege/`).

## Module Structure

```
webprotege-shared-core    # Primitive types; no GWT dependency
webprotege-shared         # Action/Result DTOs shared by client+server; compiled at Java 8 for GWT
webprotege-server-api     # Server-side repository and service interfaces
webprotege-server-lucene  # Lucene-backed entity search and short-form dictionary
webprotege-server-core    # Core business logic, action handlers, OWL API integration, MongoDB repos
webprotege-server         # WAR artifact; Dagger root wiring, Jersey REST API, servlet registration
webprotege-client         # GWT 2.8.2 client; MVP pattern, compiled to JavaScript
webprotege-cli            # Command-line tools (admin account creation, permission rebuild, etc.)
```

Dependency direction: `webprotege-shared-core` ← `webprotege-shared` ← `webprotege-server-api` ← `webprotege-server-core` ← `webprotege-server`.  
The client depends on `webprotege-shared` only (no server code).

## Key Architecture

### Action/Result Dispatch (Command Pattern)
All client–server communication uses a command-bus pattern defined in `webprotege-shared`:
- `Action<R extends Result>` — typed command sent from client
- `Result` — typed response
- GWT RPC `DispatchService` at `/webprotege/dispatchservice` routes calls to `ActionHandlerRegistry`
- Each handler implements `ActionHandler<A, R>` (stateless, thread-safe); project-scoped handlers extend `AbstractProjectActionHandler`
- Mutation handlers extend `AbstractProjectChangeHandler`, which orchestrates: generate OWL changes → apply → persist revision → emit events

### Mutation Flow
Every ontology edit follows this path:
1. Action handler calls `HasApplyChanges.applyChanges(userId, changeListGenerator)`
2. `ChangeListGenerator` produces a list of OWL API `OWLOntologyChange` objects
3. Changes are applied to the in-memory ontology, stored as a binary revision (BinaryOWL format on disk), and broadcast as `ProjectEvent`s
4. All in-memory axiom indices (see below) are updated atomically

### In-Memory Ontology Indices
Each loaded project maintains its entire ontology in RAM via fine-grained index classes in `webprotege-server-core/.../server/index/impl/`. These are the primary read path — MongoDB is **not** queried for ontology axioms. Indices are rebuilt from the BinaryOWL revision log on project load and updated incrementally on every write.

### Dependency Injection (Dagger 2)
- **Server**: `ServerComponent` (root singleton) created at servlet context init; `ProjectComponent` (Dagger `@Subcomponent`) created per project and cached in `ProjectCache`.
- **Client**: `ClientApplicationComponent` / `ClientProjectComponent` (GWT-compatible Dagger).
- Scopes: `@ApplicationSingleton` for app-wide beans, `@ProjectSingleton` for per-project beans.

### Jersey REST API
Exposed at `/data/*`, implemented in `webprotege-server/.../server/api/`. Uses API key authentication (`AuthenticationFilter`). Resources include projects, axioms, forms, revisions, and project settings.

### GWT Client (MVP)
Entry point: `WebProtege.java` → `WebProtegeInitializer`. Each feature has a Presenter class and a View interface with a `*.ui.xml` UiBinder implementation. `PlaceHistoryHandler` manages browser URL-based navigation.

## Configuration

Runtime configuration is loaded from `webprotege.properties` on the classpath. Key properties:
- `data.directory` (required) — filesystem path where ontologies and uploads are stored (default in dev: `/srv/webprotege`)
- `mongodb.host` / `mongodb.port` — MongoDB connection (defaults: `localhost:27017`)

To override in development, place a `webprotege.properties` file in `webprotege-server/src/main/webapp/WEB-INF/classes/`.

## Testing Conventions

- Test classes are named `*_TestCase.java`
- Stack: JUnit 4, Mockito, Hamcrest
- JVM flag `--add-opens java.base/java.lang=ALL-UNNAMED` is applied globally via `maven-surefire-plugin` (required for Mockito on Java 11)
- GWT client tests (`*_TestCase.java` in `webprotege-client`) run as plain JUnit (no GWT test runner needed)

## Technology Stack

| Concern | Technology |
|---------|-----------|
| Frontend | GWT 2.8.2 (compiled to JS) |
| Server | Java 11, Servlets, Jersey 2.27 (JAX-RS) |
| OWL processing | OWL API 4.5.13, BinaryOWL |
| Database | MongoDB 4.x via Morphia 1.3 ODM |
| Search | Apache Lucene (webprotege-server-lucene) |
| DI | Dagger 2.20 |
| Serialization | Jackson 2.11, Gson |
| Caching | Caffeine |
| Email templates | Mustache |
| Build | Maven 3.5+ |
| Deployment | Tomcat 8/8.5, Docker |

## Branch Strategy

- Default branch (`main`/`master`): production-ready, no direct pushes (PR + CI required)
- Branch format: `<type>/<optional-ticket>-<kebab-desc>`
  - Types: `feature`, `fix`, `hotfix`, `test`, `refactor`, `docs`, `chore`, `release`
- Commits / PR titles: Conventional Commits (`feat|fix|docs|...`)
- GitHub Issue/PR/Review comments: Conventional Comments (`suggestion:` / `issue (blocking):` / ...)
- Cursor enforcement: `.cursor/rules/git-branch-commit.mdc` + skill `git-naming-conventions`

## Connect to RAG server

### 1. Connection Parameters
| Key | Value | Environment Variable Override |
|----|-----|--------------|
| Host | `192.168.3.231` | `REMOTE_HOST` |
| SSH User | `guofengxu` | `REMOTE_USER` |
| Application Root Directory | `/home/guofengxu/00.workspace/industrialAIAgent` | `REMOTE_DIR` |
| Authentication Method | SSH Public Key (Recommended) | — |
### 3. Basic Connection
#### 3.1 Interactive Login
​```bash
ssh guofengxu@192.168.3.231
​```
#### 3.2 Non-Interactive Connectivity Check
​```bash
ssh guofengxu@192.168.3.231 'hostname && whoami'
​```
Expected: Outputs the hostname and `guofengxu`.
#### 3.3 Enter the Application Directory
​```bash
ssh guofengxu@192.168.3.231 'cd /home/guofengxu/00.workspace/industrialAIAgent && pwd'
​```
---
