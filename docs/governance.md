# Governance, Architecture, and DoD

## Testing Policy

Required on every PR:

- Lint
- Build
- Unit tests
- ATP tests
- Governance checks (branch name, one commit, PR template sections, issue link)

Main and nightly:

- ATP full run
- Emulator flow suite after Android scaffold is added

## Architecture Contracts

- Stack: Kotlin, Compose, MVVM, StateFlow, Hilt
- Multi-module from day one
- Strict flow: Screen -> ViewModel -> UseCase -> Repository
- No Screen -> Repository direct calls
- No ViewModel -> Repository direct calls without UseCase
- UI components must be modular and reusable, built in `:ui-components`
- Each new reusable UI component is delivered in its own PR, not bundled with feature logic PRs

## Module Baseline

- `:app`
- `:ui-components`
- `:feature-ultra-mode`
- `:feature-allowlist`
- `:feature-estimation`
- `:core-system`
- `:core-data`
- `:testing-atp`

`core-system` exposes interfaces (e.g. `SystemProfileRepository`, `AppLaunchRepository`) so
implementations can be swapped without feature-layer rewrites. This is no longer just a future
possibility: `:core-system`/`:core-data` now also contain real root-shell-backed implementations
(`RootSystemProfileRepositoryImpl`, `RootProcessControlRepositoryImpl`, gated by
`RootCapabilityRepository`), selected at runtime via a Hilt `@Provides` module — feature modules
are unaware of which implementation is active.

`ui-components` contains shared, reusable Compose UI: theme tokens (black minimalist theme), buttons, toggles, cards, list rows. Feature modules depend on `ui-components` but never duplicate shared UI logic locally.

## Definition of Done

A PR is done only when:

1. Linked issue present.
2. Branch naming follows convention.
3. Required CI checks are green.
4. Unit and ATP coverage exists for changed behavior.
5. ATP scenarios updated or justified as not impacted.
6. PR includes clear test steps.
7. Documentation updated.
8. Diagrams updated when behavior or architecture changed.
9. New reusable UI components live in `:ui-components` and ship in their own dedicated PR.

## Release Policy

- Versioning: `v0.x.y` during build-out
- Release gate: required checks green, ATP green, manual sanity on Nothing Phone 3a Pro
