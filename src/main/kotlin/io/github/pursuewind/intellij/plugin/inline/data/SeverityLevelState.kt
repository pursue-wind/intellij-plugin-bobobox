package io.github.pursuewind.intellij.plugin.inline.data

import com.intellij.util.xmlb.annotations.Attribute
import java.awt.Color
import java.io.Serializable
import java.util.*

open class SeverityLevelState : Serializable {
    constructor()
    constructor(
        showGutterIcon: Boolean, showText: Boolean, showBackground: Boolean, showEffect: Boolean,
        textColor: Color, backgroundColor: Color, effectColor: Color
    ) {
        this.showGutterIcon = showGutterIcon
        this.showText = showText
        this.showBackground = showBackground
        this.showEffect = showEffect
        this.textColor = textColor
        this.backgroundColor = backgroundColor
        this.effectColor = effectColor
    }

    constructor(textColor: Color, backgroundColor: Color, effectColor: Color) : this(
        true,
        true,
        true,
        true,
        textColor,
        backgroundColor,
        effectColor
    )

    constructor(other: SeverityLevelState) {
        showGutterIcon = other.showGutterIcon
        showText = other.showText
        showBackground = other.showBackground
        showEffect = other.showEffect
        textColor = other.textColor
        backgroundColor = other.backgroundColor
        effectColor = other.effectColor
    }

    @JvmField
    var showGutterIcon = true
    @JvmField
    var showText = true
    @JvmField
    var showBackground = true
    @JvmField
    var showEffect = true

    @JvmField
    @Attribute(converter = MyColorConverter::class)
    var textColor = Color.RED

    @JvmField
    @Attribute(converter = MyColorConverter::class)
    var backgroundColor = Color.GREEN

    @JvmField
    @Attribute(converter = MyColorConverter::class)
    var effectColor = Color.ORANGE
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SeverityLevelState
        return showGutterIcon == that.showGutterIcon && showText == that.showText && showBackground == that.showBackground && showEffect == that.showEffect && textColor == that.textColor && backgroundColor == that.backgroundColor && effectColor == that.effectColor
    }

    override fun hashCode(): Int {
        return Objects.hash(
            showGutterIcon,
            showText,
            showBackground,
            showEffect,
            textColor,
            backgroundColor,
            effectColor
        )
    }

    override fun toString(): String {
        return "SeverityLevelState{" +
                "showGutterIcon=" + showGutterIcon +
                ", showText=" + showText +
                ", showBackground=" + showBackground +
                ", showEffect=" + showEffect +
                ", textColor=" + textColor +
                ", backgroundColor=" + backgroundColor +
                ", effectColor=" + effectColor +
                '}'
    }
}
