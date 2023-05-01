package io.github.pursuewind.intellij.plugin.inline.domain

import io.github.pursuewind.intellij.plugin.inline.data.MySettingsService
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeEvent
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeListener
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeObservable

interface NumberOfWhitespaces {

    val numberOfWhitespaces: Int


    object BySettings : NumberOfWhitespaces, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var currentNumberOfWhitespaces: Int = 0

        override val numberOfWhitespaces: Int
            get() = currentNumberOfWhitespaces

        override fun onSettingsChange(event: SettingsChangeEvent) {
            currentNumberOfWhitespaces = event.newSettingsState.numberOfWhitespaces
        }

    }
}