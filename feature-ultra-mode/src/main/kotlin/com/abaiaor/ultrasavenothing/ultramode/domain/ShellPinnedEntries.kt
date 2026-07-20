package com.abaiaor.ultrasavenothing.ultramode.domain

/**
 * Package name identifiers for the two entries that are always shown, pinned, in the minimal
 * Ultra Mode shell (Phone and Messages). These intentionally mirror
 * `com.abaiaor.ultrasavenothing.allowlist.domain.PinnedApps` in `:feature-allowlist` — the two
 * values must stay identical since they're the same conceptual identifiers, but are duplicated
 * here rather than introducing a `:feature-ultra-mode` -> `:feature-allowlist` module
 * dependency, keeping each feature module's dependencies limited to `:core-*`/`:ui-components`.
 */
object ShellPinnedEntries {
    const val PHONE = "pinned.phone"
    const val SMS = "pinned.sms"
}
