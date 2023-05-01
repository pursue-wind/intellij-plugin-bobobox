package io.github.pursuewind.intellij.plugin.inline.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import io.github.pursuewind.intellij.plugin.inline.data.MySettingsService
import io.github.pursuewind.intellij.plugin.inline.ui.settings.SettingsComponentProvider

class MyConfigurable : SearchableConfigurable {
    private val settingsService = ApplicationManager.getApplication().getService(MySettingsService::class.java)
    private val settingsComponentProvider: SettingsComponentProvider =
        SettingsComponentProvider.Main(settingsService.state)

    override fun getHelpTopic() = "Help topic"
    override fun getId(): String = "pursuewind.plugin.shitbox"

    override fun getDisplayName() = "InLine Settings"

    override fun createComponent() = settingsComponentProvider.createComponent()

    override fun isModified() = settingsService.state != settingsComponentProvider.getState()

    override fun apply() {
        val newState = settingsComponentProvider.getState()
        settingsService.loadState(newState)
        MySettingsService.OBSERVABLE.notify(newState)
    }
}
