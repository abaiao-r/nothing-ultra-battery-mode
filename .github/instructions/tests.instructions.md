---
applyTo: "**/*.kt, **/*.feature"
description: "Unit test and ATP acceptance test conventions"
---

# Tests

## Unit tests

- File naming: `[ClassUnderTest]Test.kt`, colocated by package under the module's `src/test/`.
- Use JUnit and MockK-style fakes/mocks for `Repository` interfaces (constructor-injected fakes
  preferred over reflection-based mocking).
- Scope: `UseCase` business logic, formulas (e.g. battery estimate penalty), mappers. Do not unit
  test simple data classes or trivial getters.
- Test method naming: descriptive `` `WHEN condition THEN expected result` `` backtick style is
  preferred for clarity.

## ATP (Acceptance Tests Platform)

- Scenario files: `.feature` files under the `:testing-atp` module, using Gherkin
  (`Given/When/Then`).
- Step definitions: Kotlin classes annotated with `@Given`/`@When`/`@Then`, matching the exact
  Gherkin phrase (see `docs/atp/conventions.md`).
- ATP scenarios exercise real Screens/ViewModels/UseCases with fake `Repository` implementations
  at the boundary (system actions, DataStore), never with mocked ViewModels.
- Naming convention for scenario IDs: `ATP-<area>-<number>` (e.g. `ATP-ULTRA-001`,
  `ATP-ALLOWLIST-002`, `ATP-ESTIMATE-001`). Keep `docs/atp/scenarios.md` in sync when adding or
  changing scenarios.
- Run with: `./gradlew testDebugUnitTest -Patp`

## What NOT to cover with ATP

Pure, stateless UI components in `:ui-components` (e.g. `UltraModeToggle`, `BatteryEstimateCard`,
`AppPickerRow`) are covered by Compose previews and component-level tests, not ATP. ATP coverage
begins once a component is wired into a real screen/flow in a feature module.

## Required for every PR

Every PR must state, in the "ATP Scenarios" section of the PR template, which scenario IDs were
added or changed, or explicitly write `not impacted` with a one-line reason (e.g. "pure UI
component, no user flow change").
