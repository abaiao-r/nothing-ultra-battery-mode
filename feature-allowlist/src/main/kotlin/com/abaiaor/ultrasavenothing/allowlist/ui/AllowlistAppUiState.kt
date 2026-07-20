package com.abaiaor.ultrasavenothing.allowlist.ui

import com.abaiaor.ultrasavenothing.uicomponents.picker.AppPickerRowState

/**
 * A single row's worth of data for [AllowlistScreen]: the app identity plus its current
 * addable/added/locked state, derived from the live allowlist and pinned-app rules.
 */
data class AllowlistAppUiState(
    val packageName: String,
    val label: String,
    val state: AppPickerRowState,
)
