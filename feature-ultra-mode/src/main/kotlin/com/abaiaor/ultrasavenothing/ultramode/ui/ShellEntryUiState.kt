package com.abaiaor.ultrasavenothing.ultramode.ui

/**
 * A single row's worth of data for [UltraShellScreen]: the app identity, display label, and
 * whether it's one of the always-present pinned entries (Phone/Messages).
 */
data class ShellEntryUiState(
    val packageName: String,
    val label: String,
    val isPinned: Boolean,
)
