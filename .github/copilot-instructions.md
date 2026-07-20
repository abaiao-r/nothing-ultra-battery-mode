# Ultra Save Nothing: Copilot Instructions

## What is this?

An Android app (Kotlin) that replicates a Xiaomi/HyperOS-style "Ultra Battery Saver" mode for a
Nothing Phone 3a Pro. No-root first, with architecture designed to allow a future root-based
extension without rewriting feature code.

Core behavior: black minimalist UI, Calls/SMS always available, user-configurable allowlist of
extra apps (hard cap 10), a battery time estimate that drops as more apps are allowed, and a
temporary launcher-shell takeover while the mode is active.

## Stack

- Kotlin, Jetpack Compose
- Hilt for dependency injection
- MVVM with `StateFlow`
- Multi-module Gradle project (see Module Structure below)
- Min SDK: Android 13 (API 33)

## Architecture (strict)

Call flow must always be:

```
Screen -> ViewModel -> UseCase -> Repository
```

Disallowed:
- Screen calling a Repository directly
- ViewModel calling a Repository directly, bypassing a UseCase

Each layer has exactly one responsibility:
- **Screen**: UI rendering and user intents only, no business logic
- **ViewModel**: state orchestration only (`StateFlow`), delegates all business logic to UseCases
- **UseCase**: business rules, orchestrates one or more repositories
- **Repository**: data/platform access abstraction (DataStore, system services)

Full rationale and module contracts: `docs/architecture.md` and `docs/governance.md`.

## Module Structure

```
:app                    entry point, nav host, Hilt wiring, no business logic
:ui-components          shared reusable Compose UI (theme, buttons, toggles, cards, rows)
:feature-ultra-mode     Ultra Mode toggle, launcher shell
:feature-allowlist      allowed apps management
:feature-estimation     battery time estimate
:core-system            interfaces for system actions (audio, vibration, battery saver,
                        launcher role) - root-ready: a future root module can provide
                        alternate implementations without touching feature modules
:core-data              DataStore + repository implementations
:testing-atp            ATP runner/config, feature files, step definitions
```

**UI components are modular.** Every reusable Compose component lives in `:ui-components` and is
delivered in its own dedicated PR, never bundled with feature logic PRs.

## Build & Test Commands

```bash
# Build
./gradlew build

# Unit tests
./gradlew test

# ATP acceptance tests (Gherkin-based, run on Robolectric + Hilt)
./gradlew testDebugUnitTest -Patp

# Lint
./gradlew lint
```

## Testing Policy

This project uses two required test levels, intentionally not three, to avoid redundant overlap:

- **Unit tests**: pure logic (UseCases, formulas, mappers). Fast, no Android framework.
- **ATP (Acceptance Tests Platform)**: user-facing acceptance flows written in Gherkin
  (`Given/When/Then`), executed on the JVM via Robolectric + Hilt, with fake system/data
  boundaries. ATP already runs on Robolectric under the hood, so a separate standalone
  Robolectric suite is intentionally not maintained.

Rule of thumb:
- Business logic and formulas -> unit test
- A full user story/flow (e.g. "enabling Ultra Mode applies the strict profile") -> ATP scenario
- Pure, stateless UI components in `:ui-components` -> Compose previews/component tests, NOT ATP
  (ATP coverage only applies once a component is wired into a real screen/flow)

Every PR must state which ATP scenarios were added/changed, or explicitly justify "not impacted".
See `docs/atp/README.md`, `docs/atp/conventions.md`, and `docs/atp/scenarios.md`.

Emulator/device UI and full E2E tests are deferred to `main`/nightly CI runs and a manual sanity
pass on the real Nothing Phone 3a Pro before release (no device farm available).

## Git & PR Conventions

- Branch naming: `feature/<issue-id>-<short-name>` or `bugfix/<issue-id>-<short-name>`
- One issue -> one PR, one commit per branch
- No direct pushes to `main`; squash merge only
- PR must link the issue, list ATP scenario impact, and include test steps
  (see `.github/PULL_REQUEST_TEMPLATE.md`)

Full governance, Definition of Done, and release policy: `docs/governance.md`.

## Key Conventions

| Type | Suffix | Example |
|---|---|---|
| Screen (Compose) | `*Screen` | `UltraModeScreen` |
| ViewModel | `*ViewModel` | `UltraModeViewModel` |
| UseCase | `*UseCase` | `EnableUltraModeUseCase` |
| Repository | `*Repository` | `SystemProfileRepository` |
| Reusable UI component | descriptive, no forced suffix | `UltraModeToggle`, `BatteryEstimateCard` |

## Further Information

- Architecture and module contracts: `docs/architecture.md`
- Governance, DoD, release policy: `docs/governance.md`
- ATP scenario catalog and conventions: `docs/atp/`
- Path-scoped rules auto-load from `.github/instructions/*.instructions.md`
  (gradle, compose, architecture, tests)
