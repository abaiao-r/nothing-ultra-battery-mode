---
applyTo: "**/*.feature, **/acceptancetests/**/*.kt, **/AcceptanceTestRunner.kt"
description: "ATP (Acceptance Tests Platform) conventions: feature files, step definitions, runner, tags, traceability"
---

# ATP (Acceptance Tests Platform)

ATP is this project's own lightweight Gherkin-on-Robolectric-and-Hilt harness, living entirely in
`:testing-atp` and per-module `acceptancetests/` source. It is **not** a dependency on any
external/internal library — it's a small hand-built harness modeled on that same pattern
(Gherkin `.feature` files + typed step definitions + Robolectric + Hilt fakes), scoped down to
what a solo project actually needs. Full rationale and rationale-level detail: `docs/atp/conventions.md`.

## File & folder structure

```
<module>/src/testDebug/resources/features/<featureName>.feature   # Gherkin scenarios
<module>/src/testDebug/java/.../acceptancetests/
    AcceptanceTestRunner.kt                                       # one per module
    steps/
        <Feature>Steps.kt                                         # one class per .feature file
        BaseSteps.kt                                                # shared context/fakes accessor
```

- `featureName.feature` uses lowerCamelCase matching the feature's concern, e.g. `ultraModeToggle.feature`.
- `<Feature>Steps.kt` matches the feature file name, PascalCase + `Steps` suffix, e.g. `UltraModeToggleSteps.kt`.
- Every step class is `@Suppress("unused")` (methods are matched by pattern string, called via ATP's step loader, not direct references) and takes a single shared `AtpTestContext` constructor param, subclassing `BaseSteps`.

## Feature file style

- Plain Gherkin, **user-facing phrasing**, not implementation detail. Write scenarios the way you'd
  describe the behavior to a person, not the way the code calls it.
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
- `Background:` holds preconditions shared by every scenario in the file (e.g. starting state).
- **Traceability is mandatory**: put a `# ATP-<AREA>-<number> (issue #<n>)` comment directly above
  every `Scenario:` line, matching the ID in `docs/atp/scenarios.md`. A scenario without this
  comment fails review.
- **Tags**: use `@wip` above a scenario that is intentionally incomplete/not yet passing. `@wip`
  scenarios are excluded from the required PR gate (`-Patp`) but still compile and run on the
  `main`/nightly job so they aren't silently forgotten. No other tags are defined yet — don't
  invent new ones without updating this file.

## Step definition style

- Custom annotations `@Given`, `@When`, `@Then`, `@And`, `@But` (package
  `com.abaiaor.ultrasavenothing.atp.annotations` in `:testing-atp`), each taking a `pattern` string
  that must match the Gherkin step text exactly (Cucumber-expression-lite: `{string}`, `{int}`,
  `{word}` placeholders map to typed method parameters).
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
          // assert against context.fakeSystemProfileRepository / launcher shell state
      }
  }
  ```
- **Given** steps set up fake repository/system state directly through `context.fake*` accessors
  (never touch real `DataStore`/Android system APIs).
- **When** steps trigger user actions, preferably through the Compose test rule (`context.composeTestRule`)
  so the scenario exercises the real Screen -> ViewModel -> UseCase -> Repository flow, not a
  shortcut that calls a UseCase directly.
- **Then** steps assert observable outcomes: UI state via `composeTestRule`, or fake-repository
  state that reflects what the UI would show.
- Data tables are supported for tabular input (e.g. a list of allowed apps): step methods may take
  a trailing `List<String>` or `List<Map<String, String>>` parameter.

## Runner & execution

- One `AcceptanceTestRunner.kt` per module that owns ATP scenarios (e.g.
  `:feature-ultra-mode`, `:feature-allowlist`, `:feature-estimation`), annotated:
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
- Run one module's runner: `./gradlew :<module>:testDebugUnitTest -Patp --tests="*.AcceptanceTestRunner"`
- `@wip`-tagged scenarios are skipped when `-Patp` runs in the required PR gate, but included in
  the `main`/nightly extended run (see `.github/workflows/main-nightly.yml`).

## Scope boundary (what is NOT an ATP scenario)

- Pure, stateless composables in `:ui-components` -> Compose previews/component tests, not ATP.
- Pure business-logic formulas/mappers in isolation -> unit test (`.github/instructions/tests.instructions.md`), not ATP. ATP is for full user-facing flows, not for re-testing a UseCase's internals a second time.

## Traceability requirement

Every new or changed scenario must have a matching row in `docs/atp/scenarios.md`
(`ID | Area | Scenario | Linked Issues`). A PR that adds/changes a `.feature` file without
updating that table fails review.
