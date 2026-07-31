WebProtégé
==========

PLEASE NOTE
===========

**This repository is in the process of being superceded** with a collection of other, more fine-grained, [repositories](https://github.com/search?q=topic%3Awebprotege+org%3Aprotegeproject&type=Repositories). We are moving WebProtégé to a microservice architecture and each microservice and each common library now has its own repository.  While this repository still serves as the repository for the current WebProtégé release, all active development is now taking place in these repositories.  You can read more about this on the [WebProtégé Next Gen Wiki](https://github.com/protegeproject/webprotege-next-gen/wiki/WebProtégé-Next-Generation-Overview).


What is WebProtégé?
-------------------

WebProtégé is a free, open source collaborative ontology development environment.

It provides the following features:
- Support for editing OWL 2 ontologies
- A default simple editing interface, which provides access to commonly used OWL constructs
- Full change tracking and revision history
- Collaboration tools such as, sharing and permissions, threaded notes and discussions, watches and email notifications
- Customizable user interface
- Support for editing OBO ontologies
- Multiple file formats for upload and download of ontologies (supported formats: RDF/XML, Turtle, OWL/XML, OBO, and others)

WebProtégé runs as a Web application. End users access it through their Web browsers.
They do not need to download or install any software. We encourage end-users to use

https://webprotege.stanford.edu

If you have downloaded the webprotege war file from GitHub, and would like to deploy it on your own server,
please follow the instructions at:

https://github.com/protegeproject/webprotege/wiki/WebProtégé-4.0.0-beta-x-Installation

Project Architecture
--------------------

WebProtégé is a Java web application for collaborative OWL 2 ontology editing. The system uses a **command-bus pattern** for client–server communication, **in-memory axiom indices** for fast reads, and **BinaryOWL revision logs** on disk as the source of truth for ontology changes.

### High-Level Overview

```
┌─────────────────────────────────────────────────────────────────┐
│  webprotege-client (GWT 2.8.2 → JavaScript)                     │
│  MVP Presenters/Views · GWT RPC · URL-based navigation          │
└────────────────────────────┬────────────────────────────────────┘
                             │ /webprotege/dispatchservice
┌────────────────────────────▼────────────────────────────────────┐
│  webprotege-server (WAR)                                        │
│  Servlets · Jersey REST /data/* · Dagger ServerComponent        │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│  webprotege-server-core                                         │
│  ActionHandlerRegistry · ChangeManager · ProjectCache · Indices │
└──────┬──────────────────────┬──────────────────────┬───────────┘
       │                      │                      │
  MongoDB (metadata)    File system (BinaryOWL)   Lucene (search)
```

**Two communication channels:**
- **GWT RPC** — primary UI ↔ server interaction via the command bus
- **Jersey REST** — external API at `/data/*` (API key authentication)

### Maven Modules

The project is a Maven multi-module build (version `5.0.0-SNAPSHOT`) with eight submodules:

| Module | Responsibility |
|--------|----------------|
| `webprotege-shared-core` | Primitive types; no GWT dependency |
| `webprotege-shared` | Action/Result DTOs shared by client and server; compiled at Java 8 for GWT |
| `webprotege-server-api` | Server-side repository and service interfaces |
| `webprotege-server-lucene` | Lucene-backed entity search and short-form dictionary |
| `webprotege-server-core` | Core business logic, action handlers, OWL API integration, MongoDB repos |
| `webprotege-server` | WAR artifact; Dagger root wiring, Jersey REST API, servlet registration |
| `webprotege-client` | GWT 2.8.2 client; MVP pattern, compiled to JavaScript |
| `webprotege-cli` | Command-line tools (admin account creation, permission rebuild, etc.) |

**Dependency direction:**

```
webprotege-shared-core ← webprotege-shared ← webprotege-server-api ← webprotege-server-lucene
                                                              ↓
                                                        webprotege-server-core ← webprotege-server (WAR)
                                                              ↑
                                                         webprotege-cli
webprotege-client → webprotege-shared only (no server code)
```

### Server Startup and Request Routing

On startup, `WebProtegeServletContextListener` creates the Dagger `ServerComponent` and registers servlets:

| Path | Purpose |
|------|---------|
| `/` → `WebProtege.jsp` | GWT single-page application entry |
| `/webprotege/dispatchservice` | GWT RPC command bus |
| `/webprotege/submitfile` | File upload |
| `/download` | Ontology download |
| `/data/*` | Jersey REST API |

### Action/Result Command Bus

All client–server communication uses a command pattern defined in `webprotege-shared`:

- `Action<R extends Result>` — typed command sent from the client
- `Result` — typed response
- GWT RPC `DispatchService` routes calls to `ActionHandlerRegistry`
- Each handler implements `ActionHandler<A, R>` (stateless, thread-safe)
- Project-scoped handlers extend `AbstractProjectActionHandler`
- Mutation handlers extend `AbstractProjectChangeHandler`, which orchestrates: generate OWL changes → apply → persist revision → emit events

**Mutation flow for every ontology edit:**

1. Action handler calls `HasApplyChanges.applyChanges(userId, changeListGenerator)`
2. `ChangeListGenerator` produces a list of OWL API `OWLOntologyChange` objects
3. Changes are applied to the in-memory ontology, stored as a binary revision (BinaryOWL format on disk), and broadcast as `ProjectEvent`s
4. All in-memory axiom indices are updated atomically

### Dependency Injection (Dagger 2)

- **Server**: `ServerComponent` (root singleton) created at servlet context init; `ProjectComponent` (Dagger `@Subcomponent`) created per project and cached in `ProjectCache`
- **Client**: `ClientApplicationComponent` / `ClientProjectComponent` (GWT-compatible Dagger)
- Scopes: `@ApplicationSingleton` for app-wide beans, `@ProjectSingleton` for per-project beans

### In-Memory Ontology Indices

Each loaded project maintains its entire ontology in RAM via fine-grained index classes in `webprotege-server-core/.../server/index/impl/` (50+ index implementations). These are the primary read path — **MongoDB is not queried for ontology axioms**. Indices are rebuilt from the BinaryOWL revision log on project load and updated incrementally on every write via `IndexUpdater`.

### Project Lifecycle

`ProjectCache` manages loaded projects:

- Caches `ProjectComponent` instances in a `ConcurrentHashMap<ProjectId, ProjectComponent>`
- Purges dormant projects after a configurable idle period (default: 180 seconds)
- On load, replays all BinaryOWL revisions through OWL API to rebuild the in-memory ontology and indices

### Persistence

| Storage | Technology | Contents |
|---------|------------|----------|
| MongoDB | Morphia 1.3 ODM | Users, permissions, project metadata, discussions, tags, form config |
| File system | BinaryOWL format | Ontology revision log (`data.directory`, default `/srv/webprotege`) |
| Lucene | `webprotege-server-lucene` | Entity short-form search, multilingual dictionary |

### Client Architecture (GWT MVP)

Entry point: `WebProtege.java` → `WebProtegeInitializer` → `ApplicationPresenter`. Each feature has a Presenter class and a View interface with a `*.ui.xml` UiBinder implementation. `PlaceHistoryHandler` manages browser URL-based navigation (e.g. `#project/xxx`).

Major client packages mirror server-core packages: `hierarchy`, `frame`, `form`, `perspective`, `search`, `match`, `viz`, `obo`, `bulkop`, `sharing`, `watches`, and `library/` (shared UI widgets).

### Jersey REST API

Exposed at `/data/*`, implemented in `webprotege-server/.../server/api/`. Uses API key authentication (`AuthenticationFilter`). Resources include:

- `ProjectsResource` / `ProjectResource` — project management
- `AxiomsResource` / `DeleteAxiomsResource` — axiom read/write
- `FormsResource` / `FormResource` — form configuration
- `RevisionsResource` — revision history
- `ProjectSettingsResource` — project settings

### Technology Stack

| Concern | Technology |
|---------|------------|
| Frontend | GWT 2.8.2 (compiled to JS) |
| Server | Java 11, Servlets, Jersey 2.27 (JAX-RS) |
| OWL processing | OWL API 4.5.13, BinaryOWL |
| Database | MongoDB 4.x via Morphia 1.3 ODM |
| Search | Apache Lucene |
| DI | Dagger 2.20 |
| Serialization | Jackson 2.11, Gson |
| Caching | Caffeine |
| Email templates | Mustache |
| Build | Maven 3.5+ |
| Deployment | Tomcat 8/8.5, Docker |

### Configuration

Runtime configuration is loaded from `webprotege.properties` on the classpath. Key properties:

- `data.directory` (required) — filesystem path where ontologies and uploads are stored (default in dev: `/srv/webprotege`)
- `mongodb.host` / `mongodb.port` — MongoDB connection (defaults: `localhost:27017`)
- `project.dormant.time` — idle time before a loaded project is purged from cache (default: `180000` ms)

To override in development, place a `webprotege.properties` file in `webprotege-server/src/main/webapp/WEB-INF/classes/`.

### Repository Layout

```
webprotege/
├── pom.xml                    # Parent POM
├── docker-compose.yml         # Docker orchestration
├── Dockerfile                 # Multi-stage image build
├── deployment/                # Packaging scripts
├── webprotege-shared-core/    # Primitive types
├── webprotege-shared/         # Action/Result DTOs
├── webprotege-server-api/     # Server interfaces
├── webprotege-server-lucene/  # Lucene search
├── webprotege-server-core/    # Core business logic
├── webprotege-server/         # WAR + Servlets + REST
├── webprotege-client/         # GWT frontend
└── webprotege-cli/            # Command-line tools
```

Building
--------

To build WebProtégé from source

1) Clone the github repository
   ```
   git clone https://github.com/protegeproject/webprotege.git
   ```
2) Open a terminal in the directory where you clone the repository to
3) Use maven to package WebProtégé
   ```
   mvn clean package
   ```
   For a faster server-only build (skips GWT compilation, which takes ~10–15 minutes):
   ```
   mvn clean package -P skip-gwt-compilation
   ```
4) The WebProtege `.war` file will be built into the `webprotege-server/target` directory

Testing
-------

```bash
# Run all tests
mvn test

# Run tests for a specific module
mvn test -pl webprotege-server-core

# Run a single test class
mvn test -pl webprotege-server-core -Dtest=SomeClass_TestCase
```

Test classes are named `*_TestCase.java`. The stack is JUnit 4, Mockito, and Hamcrest. GWT client tests run as plain JUnit (no GWT test runner needed).

Running from Maven
------------------

To run WebProtégé in SuperDev Mode using maven

1) Start the GWT code server in one terminal window
    ```
    mvn gwt:codeserver
    ```
2) In a different terminal window start the tomcat server
    ```
    mvn -Denv=dev tomcat7:run
    ```
3) Browse to WebProtégé in a Web browser by navigating to [http://localhost:8080](http://localhost:8080)

Running from Docker
-------------------

To run WebProtégé using Docker containers:

1. Enter this following command in the Terminal to start the docker container in the background

   ```bash
   docker-compose up -d
   ```

2. Create the admin user (follow the questions prompted to provider username, email and password)

   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```

3. Browse to WebProtégé Settings page in a Web browser by navigating to [http://localhost:5000/#application/settings](http://localhost:5000/#application/settings)
   1. Define the `System notification email address` and `application host URL`
   2. Enable `User creation`, `Project creation` and `Project import`

To stop WebProtégé and MongoDB:

   ```bash
   docker-compose down
   ```

Sharing the volumes used by the WebProtégé app and MongoDB allow to keep persistent data, even when the containers stop. Default shared data storage:

* WebProtégé will store its data in the source code folder at `./.protegedata/protege` where you run `docker-compose`
* MongoDB will store its data in the source code folder at `./.protegedata/mongodb` where you run `docker-compose`

> Path to the shared volumes can be changed in the `docker-compose.yml` file.

The `Dockerfile` uses a multi-stage build: Maven on JDK 11 compiles the project, then Tomcat 8 on JRE 11 runs the resulting `webprotege.war` and `webprotege-cli.jar`.
