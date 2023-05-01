package io.github.pursuewind.intellij.plugin.inline.domain.settings

import io.github.pursuewind.intellij.plugin.inline.data.SettingsState

interface SettingsChangeEvent {
    val newSettingsState: SettingsState
}

class SettingsChangeEventImpl(
    override val newSettingsState: SettingsState,
) : SettingsChangeEvent