package io.github.pursuewind.intellij.plugin.inline.domain.settings

import io.github.pursuewind.intellij.plugin.inline.data.SettingsState

interface SettingsChangeObservable {

    fun subscribe(listener: SettingsChangeListener, priority: Priority)

    fun unsubscribe(listener: SettingsChangeListener)

    fun notify(newSettingsState: SettingsState)

    enum class Priority { FIRST, DEFAULT, LAST }
}