package io.github.pursuewind.intellij.plugin.inline.domain.settings


interface SettingsChangeListener {

    fun onSettingsChange(event: SettingsChangeEvent)
}