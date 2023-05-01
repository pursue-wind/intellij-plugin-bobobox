package io.github.pursuewind.intellij.plugin.inline.data

import java.io.Serializable
import java.util.*

class SettingsState : Serializable {
    @JvmField
    var numberOfWhitespaces = DefaultSettings.NUMBER_OF_WHITESPACES

    @JvmField
    var effectType = DefaultSettings.EFFECT_TYPE

    @JvmField
    var maxErrorsPerLine = DefaultSettings.MAX_ERRORS_PER_LINE

    @JvmField
    var font = DefaultSettings.FONT

    @JvmField
    var textStyle = DefaultSettings.TEXT_STYLE

    @JvmField
    var oneGutterMode = DefaultSettings.ONE_GUTTER_MODE

    @JvmField
    var error = DefaultSettings.ERROR

    @JvmField
    var warning = DefaultSettings.WARNING

    @JvmField
    var weakWarning = DefaultSettings.WEAK_WARNING

    @JvmField
    var information = DefaultSettings.INFORMATION

    @JvmField
    var serverError = DefaultSettings.SERVER_ERROR

    @JvmField
    var otherError = DefaultSettings.OTHER_ERROR

    @JvmField
    var ignoreList = DefaultSettings.IGNORE_LIST
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SettingsState
        return numberOfWhitespaces == that.numberOfWhitespaces && maxErrorsPerLine == that.maxErrorsPerLine && error == that.error && warning == that.warning && weakWarning == that.weakWarning && information == that.information && serverError == that.serverError && otherError == that.otherError &&
                ignoreList.contentEquals(that.ignoreList) && effectType == that.effectType && font == that.font && textStyle == that.textStyle && oneGutterMode == that.oneGutterMode
    }

    override fun hashCode(): Int {
        var result = Objects.hash(
            numberOfWhitespaces, maxErrorsPerLine, error, warning, weakWarning,
            information, serverError, otherError, effectType, font, textStyle, oneGutterMode
        )
        result = 31 * result + Arrays.hashCode(ignoreList)
        return result
    }

    override fun toString(): String {
        return """
            SettingsState{
            numberOfWhitespaces=$numberOfWhitespaces
            effectType=$effectType
            maxErrorsPerLine=$maxErrorsPerLine
            font=$font
            textStyle=$textStyle
            oneGutterMode=$oneGutterMode
            error=$error
            warning=$warning
            weakWarning=$weakWarning
            information=$information
            serverError=$serverError
            otherError=$otherError
            ignoreList=${ignoreList.contentToString()}}
            """.trimIndent()
    }

    companion object {
        val NONE = SettingsState()
    }
}