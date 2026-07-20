# ATP Conventions

This is the canonical reference for how ATP (Acceptance Tests Platform) works in this repo.
Copilot-facing rules derived from this doc live in `.github/instructions/atp.instructions.md` —
keep both in sync when the conventions change.

## What ATP is (and isn't)

ATP is a small, hand-built harness combining Gherkin `.feature` files with typed step
definitions, executed on the JVM via Robolectric + Hilt, using fake repositories/system
boundaries instead of the real Android system. It exists in `:testing-atp` and per-module
`acceptancetests/` source sets.

It is **not** a dependency on any third-party or company-internal ATP library — this project has
no access to those, and doesn't need their full feature set. It reuses the same *pattern*
(Gherkin + Robolectric + Hilt + fakes) at a scale appropriate for a solo hobby project: no code
generation, no annotation processors, just a small reflection-based step matcher.

It runs *on top of* Robolectric, which is why this project does not maintain a separate,
standalone Robolectric test suite — that would be redundant coverage of the same JVM/Android
shadow layer ATP already exercises.

## Folder & file structure

```
:testing-atp/                                                  # shared ATP harness code
    src/main/kotlin/.../atp/
        annotations/            Given.kt, When.kt, Then.kt, And.kt, But.kt
        core/                   AtpTestContext.kt, FeatureRunner.kt, StepLoader.kt

<feature-module>/src/testDebug/
    resources/features/<featureName>.feature
    java/.../acceptancetests/
        AcceptanceTestRunner.kt
        steps/
            BaseSteps.kt
            <Feature>Steps.kt
```

- One `.feature` file per cohesive user flow (not per screen, not per method).
- One `AcceptanceTestRunner.kt` per feature module that owns ATP scenarios.
- One `<Feature>Steps.kt` class per `.feature` file, holding all its step definitions.
- `BaseSteps.kt` (per module) exposes the shared `AtpTestContext`: fake repositories, the Hilt
  entry point, and the Compose test rule, so step classes never wire dependencies themselves.

## Annotations & core types

| Type | Purpose |
|---|---|
| `@Given(pattern = "...")`, `@When(pattern = "...")`, `@Then(pattern = "...")`, `@And`, `@But` | Map a step definition method to a Gherkin step text pattern. |
| `{string}`, `{int}`, `{word}` | Typed placeholders inside a `pattern`, mapped positionally to typed method parameters. |
| `AtpTestContext` | Per-scenario context: fake repositories, Compose test rule, Hilt entry point. |
| `FeatureRunner` | Loads `.feature` files, matches steps by pattern, executes scenarios. |
| `BaseSteps` | Base class every `<Feature>Steps` class extends; exposes `context`. |

## Feature file conventions

- User-facing phrasing: describe *what the user does and sees*, not the code path.
- `Background:` holds preconditions shared across every `Scenario:` in that file.
- Every `Scenario:` has a `# ATP-<AREA>-<number> (issue #<n>)` traceability comment directly above
  it, matching a row in `docs/atp/scenarios.md`.
- `@wip` tag marks a scenario that's expected to fail/incomplete; excluded from the required PR
  gate, included in the `main`/nightly extended run so it's never silently forgotten.

Example:

```gherkin
Feature: Ultra Mode toggle

Background:
  Given ultra mode is disabled

# ATP-ULTRA-001 (issue #12)
Scenario: Enabling ultra mode switches to the minimal launcher shell
  Given the user opens the ultra mode screen
  When the user enables ultra mode
  Then the launcher shell is shown
  And only calls, SMS, and the allowed apps are reachable
```

## Step definition conventions

```kotlin
@Suppress("unused")
class UltraModeToggleSteps(context: AtpTestContext) : BaseSteps(context) {

    @Given(pattern = "ultra mode is disabled")
    fun givenUltraModeDisabled() {
        context.fakeSystemProfileRepository.setUltraModeEnabled(false)
    }

    @When(pattern = "the user enables ultra mode")
    fun whenUserEnablesUltraMode() {
        context.composeTestRule.onNodeWithText("Enable").performClick()
    }

    @Then(pattern = "only calls, SMS, and {int} allowed apps are reachable")
    fun thenOnlyAllowedAppsReachable(allowedCount: Int) {
        // assert against fake repository / launcher shell state
    }
}
```

- `Given` steps mutate fake state directly (never touch real DataStore/system APIs).
- `When` steps drive the real UI via the Compose test rule wherever possible, so the scenario
  exercises the full `Screen -> ViewModel -> UseCase -> Repository` flow rather than skipping
  layers.
- `Then` steps assert observable outcomes (UI state or fake-repository state reflecting the UI).
- Data tables (`List<String>`, `List<Map<String, String>>`) are supported as trailing step
  parameters for tabular input, e.g. a list of allowed apps.

## Runner & execution

```kotlin
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = HiltTestApplication::class)
class AcceptanceTestRunner {
    @Before fun setUp() { /* prepare fakes, FeatureRunner.prepare(...) */ }
    @Test fun runFeatures() { /* FeatureRunner.runAll("features/") */ }
    @After fun tearDown() { /* FeatureRunner.tearDown() */ }
}
```

- Run all ATP scenarios: `./gradlew testDebugUnitTest -Patp`
- Run a single module's runner: `./gradlew :<module>:testDebugUnitTest -Patp --tests="*.AcceptanceTestRunner"`
- Required PR gate excludes `@wip`-tagged scenarios; `main`/nightly runs everything.

## Traceability

Every scenario ID (`ATP-<AREA>-<number>`) must appear in exactly one place: the `Scenario:`
comment in the `.feature` file, and a matching row in `docs/atp/scenarios.md` linking it to the
GitHub issue(s) it covers. A PR touching a `.feature` file without updating that table fails
review (see `code-reviewer` agent and PR template's "ATP Scenarios" section).

## Scope boundary

- Pure, stateless composables in `:ui-components` -> Compose previews/component tests, not ATP.
- Pure business-logic formulas/mappers -> unit tests, not ATP (see `.github/instructions/tests.instructions.md`).
- ATP is reserved for full, user-facing flows spanning Screen -> ViewModel -> UseCase -> Repository.
