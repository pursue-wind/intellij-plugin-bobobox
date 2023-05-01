package io.github.pursuewind.intellij.plugin.inline.domain

import io.github.pursuewind.intellij.plugin.inline.data.DefaultSettings
import io.github.pursuewind.intellij.plugin.inline.data.MySettingsService
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeEvent
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeListener
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeObservable

interface MaxErrorsPerLine {

    val maxPerLine: Int


    object BySettings : MaxErrorsPerLine, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var _maxPerLine: Int = DefaultSettings.MAX_ERRORS_PER_LINE

        override val maxPerLine: Int
            get() = _maxPerLine

        override fun onSettingsChange(event: SettingsChangeEvent) {
            _maxPerLine = event.newSettingsState.maxErrorsPerLine
        }

    }
}