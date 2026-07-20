---
applyTo: "**/src/test/**/*.kt"
description: "Unit test conventions (JVM unit tests only — see atp.instructions.md for acceptance tests)"
---

# Unit Tests

- File naming: `[ClassUnderTest]Test.kt`, colocated by package under the module's `src/test/`.
- Use JUnit and MockK-style fakes/mocks for `Repository` interfaces (constructor-injected fakes
  preferred over reflection-based mocking).
- Scope: `UseCase` business logic, formulas (e.g. battery estimate penalty), mappers. Do not unit
  test simple data classes or trivial getters.
- Test method naming: descriptive `` `WHEN condition THEN expected result` `` backtick style is
  preferred for clarity.
- Keep tests deterministic: no real network/disk I/O, inject fakes rather than mocking
  framework internals.

## What NOT to cover here

Pure, stateless UI components in `:ui-components` (e.g. `UltraModeToggle`, `BatteryEstimateCard`,
`AppPickerRow`) are covered by Compose previews and component-level tests, not unit or ATP tests.

## Required for every PR

Every PR must state, in the "ATP Scenarios" section of the PR template, which scenario IDs were
added or changed, or explicitly write `not impacted` with a one-line reason (e.g. "pure UI
component, no user flow change"). See `.github/instructions/atp.instructions.md` for full ATP
conventions and `docs/atp/conventions.md` for the canonical reference.
