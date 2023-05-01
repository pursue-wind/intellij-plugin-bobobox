package io.github.pursuewind.intellij.plugin.inline.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeEvent
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeListener
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeObservable

@State(
    name = "io.github.pursuewind.intellij.plugin.data.SettingsState",
    storages = [Storage("io.github.pursuewind.intellij.plugin.Settings.xml")]
)
class MySettingsService : PersistentStateComponent<SettingsState> {
    private val settingsState = SettingsState()
    override fun getState(): SettingsState {
        return settingsState
    }

    override fun loadState(state: SettingsState) {
        XmlSerializerUtil.copyBean(state, settingsState)
    }

    override fun initializeComponent() {
        OBSERVABLE.notify(settingsState)
    }

    companion object {
        @JvmField
        val OBSERVABLE: SettingsChangeObservable = object : SettingsChangeObservable {
            private val firstSubscribers: MutableList<SettingsChangeListener> = ArrayList()
            private val defaultSubscribers: MutableList<SettingsChangeListener> = ArrayList()
            private val lastSubscribers: MutableList<SettingsChangeListener> = ArrayList()
            override fun subscribe(listener: SettingsChangeListener, priority: SettingsChangeObservable.Priority) {
                when (priority) {
                    SettingsChangeObservable.Priority.FIRST -> firstSubscribers.add(listener)
                    SettingsChangeObservable.Priority.DEFAULT -> defaultSubscribers.add(listener)
                    SettingsChangeObservable.Priority.LAST -> lastSubscribers.add(listener)
                }
            }

            override fun unsubscribe(listener: SettingsChangeListener) {
                firstSubscribers.remove(listener)
                defaultSubscribers.remove(listener)
                lastSubscribers.remove(listener)
            }

            override fun notify(newSettingsState: SettingsState) {
                for (sub in firstSubscribers) {
                    sub.onSettingsChange(object : SettingsChangeEvent {
                        override val newSettingsState: SettingsState
                            get() = newSettingsState
                    })
                }
                for (sub in defaultSubscribers) {
                    sub.onSettingsChange(object : SettingsChangeEvent {
                        override val newSettingsState: SettingsState
                            get() = newSettingsState
                    })
                }
                for (sub in lastSubscribers) {
                    sub.onSettingsChange(object : SettingsChangeEvent {
                        override val newSettingsState: SettingsState
                            get() = newSettingsState
                    })
                }
            }
        }
    }
}
