# WebProtégé coding conventions

Use with [SKILL.md](SKILL.md) and [workflows.md](workflows.md).

---

## Naming

| Kind | Pattern | Example |
|------|---------|---------|
| Action | `Get*` / `Set*` / `Create*` / `Update*` / `Delete*` + `Action` | `CreateClassesAction` |
| Result | same stem + `Result` | `CreateClassesResult` |
| Handler | same stem + `ActionHandler` | `CreateClassesActionHandler` |
| Change generator | `*ChangeGenerator` | `CreateClassesChangeGenerator` |
| Index iface / impl | `*Index` / `*IndexImpl` | `AxiomsByTypeIndexImpl` |
| REST | `*Resource` + `*Service` + DTO | `OntologyClassResource` |
| Test | `*_TestCase` | `PerformLoginActionHandler_TestCase` |
| Test method | `should...` | `shouldFailOnTimeOut` |

One public top-level type per file. Feature packages: `shared.<feature>` / `client.<feature>` / `server.<feature>`.

---

## Java 8 vs 11

| Code | Release | Allowed | Forbidden |
|------|---------|---------|-----------|
| `shared-core`, `shared`, `client` | **8** | lambdas, streams, `Optional`, default methods | `var`, records, text blocks, switch expressions |
| `server-api`, `server-core`, `server`, lucene, integration, cli | **11** | `var` (used in `ChangeManager` and similar) | Do not leak Java 11 APIs into shared types |

GWT 2.8.2 extra bans in client/shared: `java.nio.file`, `java.net.http`, Morphia, Mongo, Lucene, Servlet, Jersey, reflection-heavy libs.

Prefer `ArrayList` / `HashMap` for classic RPC fields. Guava `ImmutableList` / `ImmutableSet` are fine (`@GwtCompatible`). Default methods on shared interfaces are OK. Records are not.

---

## GWT serialization

- Every `Action` / `Result` needs a private or protected no-arg constructor. Mark it `@GwtSerializationConstructor` (or a one-line “For serialization purposes only” Javadoc).
- Classic RPC fields must be **mutable** (non-final).
- `DispatchService` returns `DispatchServiceResultContainer`, not `Result` — GWT needs a concrete bean.
- Do not put server types (`OntologyChange`, `ChangeListGenerator`, Morphia `@Entity`) on Actions.
- Prefer `@AutoValue` + `@GwtCompatible(serializable = true)` for new shared value types:

```java
@AutoValue
@GwtCompatible(serializable = true)
public abstract class TagData implements IsSerializable {
    @JsonCreator
    public static TagData get(...) { return new AutoValue_TagData(...); }
}
```

- Optional fields: `@Nullable abstract T _getFoo()` plus `Optional<T> getFoo() { return Optional.ofNullable(_getFoo()); }`. Do not use `java.util.Optional` as an RPC field.
- AutoBean is not used. Do not introduce it.

---

## Dagger

| Component | Scope | File |
|-----------|-------|------|
| `ServerComponent` | `@ApplicationSingleton` | `webprotege-server/.../app/ServerComponent.java` |
| `ProjectComponent` | `@Subcomponent` `@ProjectSingleton` | `webprotege-server-core/.../inject/ProjectComponent.java` |
| `ClientApplicationComponent` | `@ApplicationSingleton` | `webprotege-client/.../inject/ClientApplicationComponent.java` |
| `ClientProjectComponent` | `@Subcomponent` `@ProjectSingleton` | `webprotege-client/.../inject/ClientProjectComponent.java` |

- `@Inject` ctor is enough for Dagger to *construct* a handler; you still **must** add `@Provides @IntoSet`.
- App-wide services: `@Inject` ctor, or `@Provides` in `ApplicationModule`. Repos call `ensureIndexes()`.
- Project-scoped: `@ProjectSingleton` + `@Inject`, or `@Provides` in `ProjectModule` / `IndexModule`. `HasApplyChanges` binds to `ChangeManager`.
- `@AutoFactory` for mixed injected + runtime params (`CreateClassesChangeGenerator`, Jersey sub-resources). `@Provided` marks injected deps. Do not `@Inject` those classes without the factory.
- Client Views: `@Provides View provideView(ViewImpl impl)` in `ClientProjectModule` or `ClientApplicationModule`.

---

## Comments and Javadoc

New code **must** include English comments as specified below. This was easy to skip; treat it as part of the change, not optional polish.

### Language and purpose

- Comments and Javadoc are **English only**.
- Explain **why** (intent, constraint, non-obvious contract). Do not narrate what the next line does.
- Model new types on integration-layer Javadoc (`OntologyClassResource`, `OntologyClassService`, `IndividualRuntimeData`), not on legacy author stamps.

### Required on new / changed public types

Every new public class, interface, or enum needs a class-level Javadoc that states what the type is for.

```java
/**
 * Reads OWL class frames from the live project ontology via {@link GetClassFrameAction}.
 */
public class OntologyClassService { ... }
```

```java
/**
 * Read-only REST API for OWL class frames.
 *
 * <p>Base path (under {@code /data/*}):
 * {@code /integration/projects/{projectId}/classes}</p>
 */
@Path("integration")
public class OntologyClassResource { ... }
```

REST resources must document the path under `/data/*`. Actions/Results should say what the client is requesting or returning. Indices should say which lookup they answer.

### Required on selected members

| Member | Comment |
|--------|---------|
| GWT no-arg ctor | `@GwtSerializationConstructor` and/or “For serialization purposes only” |
| Morphia no-arg ctor | Short note that Morphia requires it |
| Public method with a non-obvious contract, pagination, auth, or side effect | Method Javadoc (`{@link}`, `{@code}` for types and paths) |
| New `WebProtegePropertyName` constant | `@WebProtegePropertiesDocumentation` |
| Empty-looking `IntegrationModule` | Keep the existing class Javadoc: it must remain a valid `@Module` compilation unit |

### Do not

- Copy `Author: Matthew Horridge` / Stanford / date headers onto **new** files. Leave existing headers on files you only touch lightly.
- Comment getters, setters, `getActionClass()`, or trivial `@Override` methods.
- Restate the method name (“Gets the project id”).
- Use TODO without a reason the next reader can act on.
- Mix Chinese into Javadoc or inline comments.

### Inline comments

Use a line comment only when the **reason** is not obvious from names:

```java
// A single hard-coded page would under-report while REST count still looked like the full set.
```

If the code needs a paragraph to be understood, extract a method and put the Javadoc on that method.

### Tests

`should...` method names are the documentation. Do not add comments that repeat the assertion. A one-line comment is fine when a fixture exists only to trigger an edge case.

---

## Tests

- Class name: `Foo_TestCase.java` (underscore before `TestCase`)
- JUnit **4**, Mockito, Hamcrest — not JUnit 5 / AssertJ
- Runner: `@RunWith(MockitoJUnitRunner.class)` (`org.mockito.junit.MockitoJUnitRunner`)
- `@Mock` fields, `@Before public void setUp()`, `@Test public void should...`
- Assertions: `assertThat(x, is(...))` from Hamcrest
- Index tests: `impl.applyChanges(ImmutableList.of(AddAxiomChange.of(...)))` then assert queries

```bash
mvn test -pl webprotege-server-core
mvn test -pl webprotege-server-core -Dtest=PerformLoginActionHandler_TestCase
mvn test -pl webprotege-client
```

---

## Permissions

Override `getRequiredExecutableBuiltInAction` and/or `getRequiredExecutableBuiltInActions`. Mutations typically need `EDIT_ONTOLOGY` plus `CREATE_CLASS` (or the matching create/edit action). REST still needs a non-guest `UserId`; some operations also require an API key.

---

## Pitfalls

- Forgetting `@Provides @IntoSet` → runtime “no handler for action”.
- New index bound as the interface only, not also `@IntoSet UpdatableIndex` → never updates on write.
- Bypassing `HasApplyChanges` → indices, BinaryOWL revisions, and events go stale.
- Putting axioms in Morphia → the editor ignores them (reads come from indices).
- `var` or records in `webprotege-shared` / `webprotege-client` → Java 8 / GWT compile failure.
- Place tokenizer added only to `@WithTokenizers` and not the constructor list in `WebProtegePlaceHistoryMapper` → URL never matches.
- New Place presenter missing a getter on `ClientProjectComponent` → `WebProtegeActivityMapper` cannot obtain it.
- Mutation Result without `HasEventList` → UI does not refresh.
- Hand-editing `PortletFactoryGenerated` → overwritten by the maven plugin.
- Switching tests to JUnit 5 `MockitoExtension` or AssertJ → inconsistent with the rest of the tree.
- Applying Spring `@RestController` / Quarkus Panache patterns → this app is Servlets + Jersey 2.27 + Dagger 2 + Morphia.
