package io.github.pursuewind.intellij.plugin.inline.domain

import io.github.pursuewind.intellij.plugin.inline.data.EffectType
import io.github.pursuewind.intellij.plugin.inline.data.SeverityLevelState
import java.awt.Color
import javax.swing.Icon

class RenderData(
    showGutterIcon: Boolean,
    showText: Boolean,
    showBackground: Boolean,
    showEffect: Boolean,
    textColor: Color,
    backgroundColor: Color,
    effectColor: Color,
    numberOfWhitespaces: Int,
    maxErrorsPerLine: Int,
    effectType: EffectType,
    description: String,
    icon: Icon?,
    val textStyle: TextStyle,
    val oneGutterMode: Boolean
) : SeverityLevelState() {
    val numberOfWhitespaces: Int
    val maxErrorsPerLine: Int
    val effectType: EffectType
    val description: String
    val icon: Icon?

    init {
        this.showGutterIcon = showGutterIcon
        this.showText = showText
        this.showBackground = showBackground
        this.showEffect = showEffect
        this.textColor = textColor
        this.backgroundColor = backgroundColor
        this.effectColor = effectColor
        this.numberOfWhitespaces = numberOfWhitespaces
        this.maxErrorsPerLine = maxErrorsPerLine
        this.effectType = effectType
        this.description = description
        this.icon = icon
    }
}
