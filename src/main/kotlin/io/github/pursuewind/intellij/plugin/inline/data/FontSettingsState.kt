package io.github.pursuewind.intellij.plugin.inline.data

import java.io.Serializable
import java.util.*

class FontSettingsState(
    @JvmField var fontName: String,
    @JvmField var textSample: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as FontSettingsState
        return fontName == that.fontName && textSample == that.textSample
    }

    override fun hashCode(): Int {
        return Objects.hash(fontName, textSample)
    }
}
