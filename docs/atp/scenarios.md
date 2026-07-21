# ATP Scenario Catalog

Use this catalog for issue and PR traceability.

## Naming Convention

`ATP-<area>-<number>`

Examples:

- ATP-ULTRA-001
- ATP-ALLOWLIST-001
- ATP-ESTIMATE-001

## Status

No `.feature` files exist in this repo yet. Every row below is a **reserved, planned** scenario ID
from initial architecture setup, not evidence of an implemented/passing ATP test. See
`docs/atp/conventions.md` for the scope boundary and this known gap.

## Initial Planned Scenarios

| ID | Area | Scenario | Linked Issues |
|---|---|---|---|
| ATP-ULTRA-001 | Ultra Mode | Enable ultra mode switches to minimal launcher shell | #1 |
| ATP-ALLOWLIST-001 | Allowlist | Calls and SMS always visible and allowed apps list is enforced | #1 |
| ATP-ESTIMATE-001 | Estimation | Adding apps lowers projected battery time | #1 |

## Root-mode note

Issues #39-#41 added a real root-powered implementation behind the existing `SystemProfileRepository`
and a new `RootProcessControlRepository`, selected automatically at runtime when root is available.
No new scenario rows were added for this: the user-facing Screen -> ViewModel -> UseCase flow for
enabling/disabling Ultra Mode (`ATP-ULTRA-001`) is unchanged either way — only the platform-side
effect differs, and root-shell command construction/business rules are unit tested instead (see
scope boundary above). A root-only feature would only need a new ATP scenario if it added an actual
new user-visible flow, e.g. a future "Root Mode Active" indicator.
