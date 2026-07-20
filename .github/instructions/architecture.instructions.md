---
applyTo: "**/*.kt"
description: "Architecture boundary rules for Screen -> ViewModel -> UseCase -> Repository"
---

# Architecture Boundaries

This project enforces a strict, one-directional call flow:

```
Screen -> ViewModel -> UseCase -> Repository
```

- A `Screen` (Compose composable) may only read state from and send intents to its `ViewModel`.
  It must never call a `Repository` or `UseCase` directly.
- A `ViewModel` may only call `UseCase` classes. It must never call a `Repository` directly,
  even for a "simple" read.
- A `UseCase` may call one or more `Repository` interfaces and other `UseCase`s. Business rules
  (validation, caps, formulas) live here, not in the ViewModel or Repository.
- A `Repository` is the only layer allowed to talk to DataStore, Android system services, or any
  platform API. Repositories are defined as interfaces in the owning module (e.g. `:core-system`,
  `:core-data`) with implementations injected via Hilt.

## Root-ready design

`:core-system` repository interfaces (audio, vibration, battery saver, launcher role) must stay
free of any no-root-specific assumptions in their public API, so a future root-based
implementation can be swapped in via Hilt without changing `:feature-*` modules.

## When reviewing or writing code

Flag any of the following as an architecture violation:
- `Repository` referenced from a `Screen` or a Composable function
- `Repository` called directly from a `ViewModel` (no `UseCase` in between)
- Business logic (validation, formulas, caps like the 10-app allowlist limit) implemented
  inside a `ViewModel` or `Screen` instead of a `UseCase`
