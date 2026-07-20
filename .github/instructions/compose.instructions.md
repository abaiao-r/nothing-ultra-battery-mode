---
applyTo: "**/*.kt"
description: "Compose UI and :ui-components conventions"
---

# Compose & UI Components

- Every reusable Compose component lives in `:ui-components`, never duplicated inside a feature
  module. If two feature modules need visually similar UI, extract/generalize it into
  `:ui-components` instead of copying it.
- Composables in `:ui-components` must be stateless: accept data and event lambdas as
  parameters, hoist all state to the caller. No ViewModel or Repository access inside
  `:ui-components`.
- Every public composable accepts `modifier: Modifier = Modifier` as its first optional
  parameter, and passes it to its root element.
- Provide at least one `@Preview` per component in `:ui-components`, covering the meaningful
  visual states (e.g. on/off, pinned/unpinned, addable/added/locked).
- Theme: black minimalist theme tokens live in `:ui-components` (colors, typography, shapes).
  Feature screens consume theme tokens from `:ui-components`, never redefine their own colors.
- New reusable UI components ship in their own dedicated PR (see `docs/governance.md` DoD item 9),
  separate from the PR that wires them into a feature screen.
