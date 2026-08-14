# WebProtégé development workflows

Use with [SKILL.md](SKILL.md). Language, comments, DI, and tests: [conventions.md](conventions.md).

There is no AutoRegistry. Handlers are explicit Dagger `@Provides @IntoSet`.

---

## A. Read-only project Action

Example to copy: `GetClassFrameAction` + `GetClassFrameActionHandler`.

1. `webprotege-shared/.../shared/<feature>/GetFooAction.java`
   - `implements ProjectAction<GetFooResult>`
   - Private no-arg ctor with `@GwtSerializationConstructor`
   - Public ctor taking `ProjectId` plus payload
   - Non-final fields (GWT RPC)
   - Class Javadoc: what the client is requesting and why
2. `webprotege-shared/.../GetFooResult.java` — `implements Result`
3. `webprotege-server-core/.../GetFooActionHandler.java`
   - Extends `AbstractProjectActionHandler<GetFooAction, GetFooResult>`
   - `@Inject` ctor with `AccessManager` plus collaborators
   - `getActionClass()` → `GetFooAction.class`
   - Override `getRequiredExecutableBuiltInAction` (often `BuiltInAction.VIEW_PROJECT`)
   - `execute(...)` queries **indices**, never Mongo for axioms
4. Register in `ProjectActionHandlersModule`:

```java
@Provides @IntoSet
public ProjectActionHandler provideGetFooActionHandler(GetFooActionHandler handler) {
    return handler;
}
```

5. Client: inject `DispatchServiceManager` and call:

```java
dispatchServiceManager.execute(new GetFooAction(projectId, ...), result -> { ... });
```

6. Test: `GetFooActionHandler_TestCase` in `webprotege-server-core`.

---

## B. Ontology mutation

Example to copy: `CreateClassesAction` + `CreateClassesActionHandler` + `CreateClassesChangeGenerator`.

Do **not** apply OWL API `OWLOntologyChange` directly. Use `AddAxiomChange.of(ontologyId, axiom)` / `RemoveAxiomChange` / import and annotation change types.

1. Shared Action: typically `extends AbstractHasProjectAction<DoFooResult>`
2. Shared Result: `extends AbstractHasEventListResult<ProjectEvent<?>>` so the client event bus refreshes
3. Server `DoFooChangeGenerator` with `@AutoFactory`:
   - `@Provided` injected deps (`OWLDataFactory`, `DefaultOntologyIdManager`, indices)
   - Remaining ctor args = runtime (source text, parents, …)
   - Generated factory: `DoFooChangeGeneratorFactory`
   - `generateChanges` builds `OntologyChangeList` via `OntologyChangeList.builder().add(AddAxiomChange.of(...))`
   - Fresh entities: `FreshEntityIri` placeholders; `ChangeManager` mints real IRIs
4. Handler extends `AbstractProjectChangeHandler<Subject, DoFooAction, DoFooResult>`
   - Override `getRequiredExecutableBuiltInActions` (usually `EDIT_ONTOLOGY` plus `CREATE_CLASS` / similar)
   - `getChangeListGenerator(...)` returns `factory.create(...)`
   - `createActionResult(...)` wraps the subject plus `eventList`
5. Register in `ProjectActionHandlersModule` (`@Provides @IntoSet`)
6. Client: `dispatchServiceManager.execute(action, result -> ...)` — `DispatchServiceManager` fires `HasEventList` events on the GWT `EventBus`

Frame edits use `AbstractUpdateFrameHandler` + `FrameChangeGeneratorFactory` instead of a custom generator.

`AbstractProjectChangeHandler.execute` already: tag events → `applyChanges` → collect events → `createActionResult`. Do not bypass `HasApplyChanges`.

---

## C. Application-wide Action

Example to copy: `PerformLoginAction` + `PerformLoginActionHandler`.

- Action does **not** implement `ProjectAction`
- Handler implements `ApplicationActionHandler`
- Register in `ActionHandlersModule` as `@Provides @IntoSet ApplicationActionHandler`
- `DispatchServiceExecutorImpl` routes non-`ProjectAction` types through `ApplicationActionHandlerRegistry`

---

## D. Integration REST facade

Use when an external system needs HTTP and the Action already exists. Example: `OntologyClassResource` + `OntologyClassService`.

Layout under `webprotege-integration/.../server/integration/`:

```
dto/          JSON DTOs
service/      builds Actions, calls ActionDispatch
api/          JAX-RS resources
dispatch/     ActionDispatch port
IntegrationModule.java   keep as a valid @Module compilation unit
```

Steps:

1. DTO in `integration/dto` with Jackson annotations; class Javadoc stating the payload’s role
2. Service `@Inject ActionDispatch`; build an existing `*Action` (e.g. `GetClassFrameAction`)
3. Resource `@Path("integration")` + `@Inject` service
   - Class Javadoc must document the path under `/data/*`
   - Reject guest `UserId` with HTTP 401
4. Register the resource in `ApiModule.provideResourceConfig` (`resourceConfig.register(...)`)
5. Do **not** put handlers in this module — reuse `webprotege-server-core` handlers
6. Keep `IntegrationModule` as a real `@Module` class (an empty `.java` file fails javac)

`ActionDispatch` is bound in `ApiModule` to `ActionExecutor::execute`, so permissions and mutation flow stay on the command bus.

---

## E. GWT UI (Presenter / View / Place / Portlet)

Entry: `WebProtege` → `WebProtegeInitializer`. Dispatch from presenters via `DispatchServiceManager`.

Colocate in `webprotege-client/.../client/<feature>/`:

```
FooPresenter
FooView                 // interface, no GWT widgets
FooViewImpl             // Composite + UiBinder
FooViewImpl.ui.xml
```

`Presenter.start(AcceptsOneWidget container, EventBus eventBus)` puts the view in the container.

ViewImpl pattern:

```java
interface FooViewImplUiBinder extends UiBinder<HTMLPanel, FooViewImpl> {}
private static FooViewImplUiBinder ourUiBinder = GWT.create(FooViewImplUiBinder.class);
@UiField SomeWidget field;
@Inject
public FooViewImpl(...) {
    initWidget(ourUiBinder.createAndBindUi(this));
}
```

Bind in `ClientProjectModule` (or `ClientApplicationModule`):

```java
@Provides FooView provideFooView(FooViewImpl impl) { return impl; }
```

### Places

- Place class: often in **shared** so tokens are shared
- Tokenizer: `implements WebProtegePlaceTokenizer<P>` (`matches`, `isTokenizerFor`, `getPlace`, `getToken`)
- Register in **both** `@WithTokenizers` **and** the constructor list of `WebProtegePlaceHistoryMapper` (the annotation list is incomplete; mapping is manual)
- Map Place → Activity in `WebProtegeActivityMapper.getActivity`
- Project-scoped presenters: add a getter on `ClientProjectComponent`

### Portlets

```java
import edu.stanford.webprotege.shared.annotations.Portlet;

@Portlet(id = "portlets.Foo", title = "Foo", tooltip = "...")
public class FooPortletPresenter extends AbstractWebProtegePortletPresenter { ... }
```

`webprotege-maven-plugin` goal `generatePortletFactory` generates `PortletFactoryGenerated` + `PortletModulesGenerated`. Do not hand-edit those files.

UI feature order: Action/Handler (A or B) → View/Presenter → bind View → Place or `@Portlet`.

---

## F. New in-memory index

Interfaces: `webprotege-server-api/.../server/index/`
Impls: `webprotege-server-core/.../server/index/impl/` (`*IndexImpl` implements the iface + `UpdatableIndex`)

Add an index only if a query would scan all axioms repeatedly. Reuse `AxiomsByTypeIndex`, `AxiomsByEntityReferenceIndex`, `AnnotationAssertionAxiomsBySubjectIndex`, and similar first. Never query Mongo for OWL axioms.

1. Interface in server-api (`extends Index`); class Javadoc: which lookup it answers
2. Impl in server-core, `@ProjectSingleton`, `applyChanges(ImmutableList<OntologyChange>)`
3. Two bindings in `IndexModule`:

```java
@Provides
FooIndex provideFooIndex(FooIndexImpl impl) {
    return impl;
}

@Provides
@IntoSet
public UpdatableIndex provideFooIndexImplIntoSet(FooIndexImpl impl) {
    return impl;
}
```

If the index depends on another, implement `DependentIndex.getDependencies()` so `IndexUpdater` ranks it.

On project load, `IndexUpdater.buildIndexes()` replays revisions. On write, `ChangeManager` → `indexUpdater.updateIndexes(changes)`.

---

## G. Mongo / Morphia metadata repository

Mongo holds users, project details, sharing, tags, watches, comments, forms, perspectives, API keys, webhooks, roles — **not** ontology axioms. Axioms live in RAM indices + BinaryOWL under `data.directory`.

1. Morphia entity: no-arg ctor, `@Entity(value = "CollectionName", noClassnameStored = true)`, field constants, `@Property` / `@Indexes`
2. `FooRepository extends Repository` + `FooRepositoryImpl` with `Datastore`
3. Bind in `ApplicationModule` and call `ensureIndexes()`:

```java
@Provides
@ApplicationSingleton
public FooRepository provideFooRepository(FooRepositoryImpl impl) {
    impl.ensureIndexes();
    return impl;
}
```

4. Custom value types (`UserId`, `ProjectId`, `OWLEntity`): reuse converters already registered in `MorphiaProvider`
5. Repos are typically **application-scoped** and query by `ProjectId`, not `@ProjectSingleton`

---

## H. Configuration property

1. Add an enum constant on `WebProtegePropertyName` with `@WebProtegePropertiesDocumentation`
2. Document it in `webprotege-server-core/src/main/resources/webprotege.properties`
3. Add a getter on `WebProtegeProperties` using `getValue(MY_SETTING)`
4. Inject `WebProtegeProperties`, or add a `@Provides` in `ApplicationModule`

Overrides use prefix `webprotege.`: JVM `-Dwebprotege.my.setting=...` or env `webprotege.my.setting`. Only names in the enum are read. Required today: `data.directory`.
