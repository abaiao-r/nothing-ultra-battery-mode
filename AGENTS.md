# AGENTS.md

Guidance for AI coding agents working in this repo.

## Project State: Scaffolded

The 8-module Gradle project exists and builds (`./gradlew build`, `test`, and `lint` all
pass). `:app` launches to a placeholder screen with Hilt wired end-to-end. Feature modules
(`:feature-ultra-mode`, `:feature-allowlist`, `:feature-estimation`) and `:core-system`/
`:core-data` currently contain build config and empty source sets only — no business logic
yet, that's deliberately deferred to their own issues (see module baseline below). The
pre-scaffold Gradle-skip detection in `android-ci.yml`/`main-nightly.yml` is now obsolete
since `./gradlew` and `settings.gradle.kts` are present; CI runs real Gradle steps.

## What This App Is

Native Android app replicating a Xiaomi/HyperOS-style "Ultra Battery Saver" mode for a
Nothing Phone: black minimalist UI, calls/SMS always available, user-configurable allowlist
(hard cap **10 apps**), a live battery-time estimate that drops as apps are added, no
sound/vibration while active. **No root required for v1** — architecture must stay
root-ready (see `core-system` below) without root code actually being written yet.

## Required Architecture (enforce even before scaffold exists)

Strict unidirectional flow — **no shortcuts**:

```
Screen -> ViewModel -> UseCase -> Repository
```

- Screen -> Repository direct calls: forbidden.
- ViewModel -> Repository without a UseCase in between: forbidden.
- Stack: Kotlin, Jetpack Compose, MVVM, StateFlow, Hilt DI.
- Multi-module **from day one** — do not put feature logic in `:app`.

### Module baseline (create these, don't invent new top-level modules ad hoc)

| Module | Responsibility |
|---|---|
| `:app` | Nav host + Hilt wiring only. No business logic. |
| `:ui-components` | Shared reusable Compose UI (theme tokens/black theme, buttons, toggles, cards, list rows). |
| `:feature-ultra-mode` | Enable/disable ultra mode, launcher-shell takeover. |
| `:feature-allowlist` | Add/remove allowed apps, enforce 10-app cap. |
| `:feature-estimation` | Battery time estimate calculation/display. |
| `:core-system` | **Interfaces** for system actions (audio/vibration/battery-saver/launcher role switching) so a future root-based implementation can plug in without touching feature modules. Never implement root logic directly in a feature module. |
| `:core-data` | DataStore + repository implementations. |
| `:testing-atp` | ATP runner/config, `.feature`-style scenarios, step definitions. |

Feature modules depend on `:ui-components`; never re-implement shared UI locally — a new
reusable component always ships in `:ui-components`, in its **own dedicated PR**, separate
from the feature-logic PR that uses it.

## Testing Convention: ATP (not standard Robolectric/instrumented tests)

- **Unit tests**: pure logic — UseCases, estimation formulas.
- **ATP (Acceptance Tests Platform)**: Given/When/Then acceptance scenarios, run on the JVM
  via Robolectric + Hilt with fake backends. This is a **custom lightweight framework, not
  Cucumber/Gherkin tooling** — same pattern as other internal apps
  (tariff-management-app, emobility-plug-and-charge-app). Lives in `:testing-atp`.
- Do **not** add a separate standalone Robolectric suite — ATP already runs on Robolectric,
  so a parallel suite would be redundant.
- Pure, stateless components in `:ui-components` are **not** covered by ATP (use Compose
  previews/component tests instead); ATP coverage starts once a component is wired into a
  real screen/flow.
- ATP scenario IDs follow `ATP-<AREA>-<number>` (e.g. `ATP-ULTRA-001`), catalogued in
  `docs/atp/scenarios.md`. Every PR must update this catalog or state "not impacted".
- Local CI-equivalent commands (once scaffolded): `./gradlew lint`, `./gradlew build -x test`,
  `./gradlew test`, `./gradlew testDebugUnitTest -Patp`.

## Git/PR Workflow (enforced by CI, not just convention)

- One GitHub issue -> one branch -> one PR -> **exactly one commit** (amend, don't add commits;
  `pr-governance.yml` fails PRs with >1 commit).
- Branch name **must** match `^(feature|bugfix)/[0-9]+-[a-z0-9_-]+$`
  (e.g. `feature/12-ultra-mode-toggle`), or the governance check fails.
- PR body must use `.github/PULL_REQUEST_TEMPLATE.md` verbatim sections — the governance
  workflow greps the body for literal headers `Issue`, `ATP Scenarios`, `How to Test`, and an
  issue reference (`#123` or `/issues/123`). Missing any of these fails CI.
- No direct pushes to `main`; squash merge only.
- Required PR checks: `PR Governance` + `Android CI` (see `CONTRIBUTING.md`).

## Docs to Update Alongside Code Changes

- `docs/governance.md` — architecture contracts, DoD, module baseline.
- `docs/atp/scenarios.md` — scenario catalog, keyed by issue.
- Diagrams (when added) whenever behavior/architecture changes — this is a DoD item, not optional.

## Not This Repo's Job (yet)

- Emulator/instrumented UI tests and full E2E: deferred to `main`/nightly (`main-nightly.yml`),
  no device farm available — final sanity pass is manual on a physical Nothing Phone 3a Pro.
- Root-based system control: interfaces only via `:core-system`, no implementation in v1.
- Auto-triggering ultra mode at low battery: v1 is manual-toggle only.

