package io.github.pursuewind.intellij.plugin.inline.data

import com.intellij.icons.AllIcons
import io.github.pursuewind.intellij.plugin.inline.domain.TextStyle
import java.awt.Color

object DefaultSettings {
    const val NUMBER_OF_WHITESPACES = 0
    @JvmField
    val EFFECT_TYPE = EffectType.BOX
    const val MAX_ERRORS_PER_LINE = 5
    @JvmField
    val FONT = "Dialog.plain"
    @JvmField
    val TEXT_STYLE = TextStyle.AFTER_LINE
    const val ONE_GUTTER_MODE = true
    @JvmField
    val ERROR = defaultSeverityLevelState(Color(183, 43, 43))
    @JvmField
    val WARNING = defaultSeverityLevelState(Color(189, 115, 37))
    @JvmField
    val WEAK_WARNING = defaultSeverityLevelState(Color(183, 155, 41))
    @JvmField
    val INFORMATION = defaultSeverityLevelState(Color(61, 108, 201))
    @JvmField
    val SERVER_ERROR = defaultSeverityLevelState(Color(128, 29, 185))
    @JvmField
    val OTHER_ERROR = defaultSeverityLevelState(Color(141, 169, 169))
    @JvmField
    val IGNORE_LIST = arrayOf(
        "TODO",
        "Typo",
        "Automatically declared based on the expected type",
        "Value captured in a closure",
        "Expression should use clarifying parentheses",
        "Missing trailing comma"
    )

    private fun defaultSeverityLevelState(color: Color): SeverityLevelState {
        val backgroundColor = color.darker().darker()
        val newBackgroundColor = Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 80)
        return SeverityLevelState(
            color,
            newBackgroundColor,
            color.darker()
        )
    }

    object Icons {
        @JvmField
        val ERROR = AllIcons.General.Error
        val WARNING = AllIcons.General.Warning
        @JvmField
        val WEAK_WARNING = AllIcons.General.Warning
        val INFORMATION = AllIcons.General.Information
        @JvmField
        val SERVER_ERROR = AllIcons.General.ArrowDown
        val OTHER_ERROR = AllIcons.General.Pin_tab
    }
}
