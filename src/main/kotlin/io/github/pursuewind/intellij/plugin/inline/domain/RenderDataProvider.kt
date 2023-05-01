package io.github.pursuewind.intellij.plugin.inline.domain

import com.intellij.lang.annotation.HighlightSeverity
import io.github.pursuewind.intellij.plugin.inline.data.DefaultSettings
import io.github.pursuewind.intellij.plugin.inline.data.MySettingsService
import io.github.pursuewind.intellij.plugin.inline.data.SettingsState
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeEvent
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeListener
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeObservable
import io.github.pursuewind.intellij.plugin.inline.domain.wrapper.RangeHighlighterWrapper
import java.awt.Color

interface RenderDataProvider : HighlightersValidator {

    fun provide(highlighter: RangeHighlighterWrapper): RenderData


    object BySettings : RenderDataProvider, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var state: SettingsState = SettingsState.NONE

        override fun provide(highlighter: RangeHighlighterWrapper): RenderData {

            if (!isValid(highlighter))
                throw IllegalArgumentException("highlighters should be checked with RenderDataProvider.isValid(highlighter)")

            val (levelState, icon) = when (highlighter.priority) {
                in ERROR..Int.MAX_VALUE -> Pair(state.error, DefaultSettings.Icons.ERROR)
                in WARNING..ERROR -> Pair(state.warning, DefaultSettings.Icons.WARNING)
                in WEAK_WARNING..WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.WEAK_WARNING)
                in SERVER_ERROR..WEAK_WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.SERVER_ERROR)
                in INFORMATION..SERVER_ERROR -> Pair(state.serverError, DefaultSettings.Icons.INFORMATION)
                else -> Pair(state.otherError, DefaultSettings.Icons.OTHER_ERROR)
            }
            val bc = levelState.backgroundColor
            val backGroundColor = Color(bc.red, bc.green, bc.blue, 60)

            return RenderData(
                levelState.showGutterIcon,
                levelState.showText,
                levelState.showBackground,
                levelState.showEffect,
                levelState.textColor,
                backGroundColor,
                levelState.effectColor,
                state.numberOfWhitespaces,
                state.maxErrorsPerLine,
                state.effectType,
                highlighter.description,
                icon,
                state.textStyle,
                state.oneGutterMode
            )
        }

        override fun isValid(highlighter: RangeHighlighterWrapper): Boolean {

            var descriptionIsInIgnoreList = false
            for (str in state.ignoreList)
                if (highlighter.description.contains(str)) descriptionIsInIgnoreList = true

            return highlighter.isSufficient() && !descriptionIsInIgnoreList
        }

        override fun onSettingsChange(event: SettingsChangeEvent) {
            state = event.newSettingsState
        }
    }
}

private val ERROR = HighlightSeverity.ERROR.myVal
private val WARNING = HighlightSeverity.WARNING.myVal
private val WEAK_WARNING = HighlightSeverity.ERROR.myVal
private val SERVER_ERROR = HighlightSeverity.ERROR.myVal
private val INFORMATION = HighlightSeverity.ERROR.myVal
