package com.abaiaor.ultrasavenothing.uicomponents.picker

/**
 * Visual/interaction state of an [AppPickerRow]:
 * - [Addable]: not yet in the allowlist, shows an add control.
 * - [Added]: currently in the allowlist, shows a remove control.
 * - [Locked]: a pinned app (Phone/SMS) that can never be removed; shows a locked indicator
 *   and cannot trigger a remove callback.
 */
enum class AppPickerRowState {
    Addable,
    Added,
    Locked,
}
