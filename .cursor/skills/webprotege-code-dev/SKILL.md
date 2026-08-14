---
name: webprotege-code-dev
description: >-
  Implements WebProtégé features on the Action/Result command bus, Dagger
  wiring, GWT MVP, Jersey /data REST, in-memory OWL indices, and Morphia
  metadata repos. Use when adding or changing Java/GWT code, ActionHandlers,
  ChangeListGenerators, client Presenters/Views, integration REST under
  /data/integration, indices, Mongo repos, or webprotege.properties.
  Not for git naming, Docker deploy, or API key generation.
---

# WebProtégé Code Development

Follow this skill when implementing or changing features in this repository.
Pick a path first, then apply the matching checklist.

Step-by-step file lists: [workflows.md](workflows.md).
Language, DI, comments, tests, pitfalls: [conventions.md](conventions.md).

## When to use

- New or changed client↔server operations (`Action` / `Result` / `ActionHandler`)
- Ontology writes (`ChangeListGenerator` / `ChangeManager`)
- GWT Presenter / View / Place / Portlet
- Jersey `/data/*` or `webprotege-integration` REST
- In-memory `Index`, Morphia repository, or `webprotege.properties`

## Out of scope

- Branch / commit / PR naming → `git-naming-conventions`
- Local package-and-deploy → `deploy-webprotege`
- API key generation → `generate-apikey`
- Do **not** apply Spring Boot, Quarkus, JUnit 5, AssertJ, or Java record conventions

## Decision tree

1. **External HTTP only, and an Action already exists?** → Integration REST facade (reuse the handler; do not add a new one).
2. **GWT UI must call the server?** → Action/Handler first, Presenter second.
3. **Changing OWL axioms?** → Mutation: `AbstractProjectChangeHandler` + `ChangeListGenerator` + `HasApplyChanges`.
4. **Read-only project data?** → `AbstractProjectActionHandler`; read **indices**, never Mongo for axioms.
5. **App-wide (login, create project)?** → `ApplicationActionHandler` + `ActionHandlersModule`.
6. **Query would repeatedly scan all axioms?** → New index (iface in `server-api`, impl + `UpdatableIndex`).
7. **Users / tags / comments / other metadata?** → Morphia repo (Mongo), not BinaryOWL.
8. **New setting?** → `WebProtegePropertyName` enum + `webprotege.properties`.

Default: **Action/Handler first; REST is an optional facade.** The GWT UI and `/data` both execute the same command bus.

## Module map

Dependency direction: `shared-core` ← `shared` ← `server-api` ← `server-core` ← `server`.
The client depends on `shared` / `shared-core` **only**.

| Module | Put here | Must not put here |
|--------|----------|-------------------|
| `webprotege-shared-core` | Primitive types, `@ApplicationSingleton` / `@ProjectSingleton`, config enum. Java 8 | OWL, Mongo, GWT widgets |
| `webprotege-shared` | Action/Result, Place, Event, AutoValue DTOs. Java 8 + GWT | Servlet, Morphia, Lucene, handlers, index impls |
| `webprotege-server-api` | `Index`, `OntologyChange`, repository interfaces | UI, Morphia impls |
| `webprotege-server-lucene` | Lucene search / short forms | Handlers, UI |
| `webprotege-server-core` | Handlers, `ChangeManager`, index impls, Morphia | `*.ui.xml`, Presenters |
| `webprotege-server` | WAR: `ServerComponent`, Jersey, `DispatchServlet` | Business logic (keep thin) |
| `webprotege-client` | GWT MVP | Mongo, BinaryOWL, `ActionHandler` |
| `webprotege-integration` | `/data/integration/...` REST DTO / Resource / Service | New handlers, GWT |
| `webprotege-cli` | Admin commands | UI |

Package root: `edu.stanford.bmir.protege.web.{shared|client|server}.<feature>`. Mirror the feature name across layers.

## Hard rules

1. The client never depends on `webprotege-server*`.
2. Axiom reads/writes go through in-memory indices + `HasApplyChanges`. Mongo does **not** store OWL axioms.
3. Mutations use `AddAxiomChange` / `RemoveAxiomChange` (server-api), not raw OWL API `OWLOntologyChange`.
4. Every handler must be registered with `@Provides @IntoSet` or runtime fails with “no handler”.
5. A new index must bind **both** the interface **and** `@IntoSet UpdatableIndex`.
6. Mutation results must implement `HasEventList` (usually via `AbstractHasEventListResult`) or the UI will not refresh.
7. `shared` / `client` compile as **Java 8**. No `var`, records, text blocks, or switch expressions.
8. Action/Result types need a no-arg constructor (`@GwtSerializationConstructor`) and non-final RPC fields.
9. Tests are `*_TestCase.java` with JUnit 4 + Mockito + Hamcrest. Not JUnit 5 / AssertJ.
10. New public types and non-obvious public methods need English Javadoc that states purpose (why), not a paraphrase of the name. See [conventions.md](conventions.md#comments-and-javadoc).
11. `-P skip-gwt-compilation` is for server-side checks only. A full UI build must compile GWT (see `deploy-webprotege`).

## Workflow checklist

```
Progress:
- [ ] 1. Classify the change (decision tree)
- [ ] 2. Place types in the correct module
- [ ] 3. Follow the matching workflow in workflows.md
- [ ] 4. Wire Dagger (@Inject + @IntoSet / @Provides)
- [ ] 5. Add English Javadoc on new public types / non-obvious APIs
- [ ] 6. Add *_TestCase
- [ ] 7. Run the module tests
```

Verify:

```bash
mvn test -pl webprotege-server-core -Dtest=FooActionHandler_TestCase
mvn test -pl webprotege-shared
mvn test -pl webprotege-client
# Server-side only, no GWT:
mvn test -P skip-gwt-compilation -pl webprotege-server-core,webprotege-integration
```

## Canonical examples (copy these)

| Kind | Copy from |
|------|-----------|
| Read-only project Action | `GetClassFrameAction` + `GetClassFrameActionHandler` |
| Ontology mutation | `CreateClassesAction` + `CreateClassesActionHandler` + `CreateClassesChangeGenerator` |
| Application Action | `PerformLoginAction` + `PerformLoginActionHandler` |
| Handler registration | `ProjectActionHandlersModule` / `ActionHandlersModule` |
| Integration REST | `OntologyClassResource` + `OntologyClassService` + `ActionDispatch` |
| Client dispatch | `DispatchServiceManager.execute(...)` (e.g. `EntityTagsSelectorPresenter`) |
| Index | `AxiomsByTypeIndex` / `AxiomsByTypeIndexImpl` + `IndexModule` |
| Morphia | `WatchRecord` + `WatchRecordRepositoryImpl` + `ApplicationModule` |
| Purpose Javadoc | `OntologyClassResource`, `OntologyClassService`, `IndividualRuntimeData` |
| Test | `LogOutUserActionHandler_TestCase` |

## Additional resources

- Per-kind file checklists: [workflows.md](workflows.md)
- Serialization, DI, Java 8/11, comments, tests, pitfalls: [conventions.md](conventions.md)
