package io.github.pursuewind.intellij.plugin.inline.data

import com.intellij.util.xmlb.Converter
import java.awt.Color

class MyColorConverter : Converter<Color>() {
    override fun fromString(value: String): Color {
        return Color(value.toInt())
    }

    override fun toString(value: Color): String {
        return value.rgb.toString()
    }
}
