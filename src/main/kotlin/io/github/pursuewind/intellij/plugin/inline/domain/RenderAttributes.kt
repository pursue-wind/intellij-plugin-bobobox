package io.github.pursuewind.intellij.plugin.inline.domain

import io.github.pursuewind.intellij.plugin.inline.data.FontData

interface RenderAttributes {

    val offSetFromLineStart: Int

    val editorFontMetricsProvider: FontData


    class Impl(
        override val offSetFromLineStart: Int,
        override val editorFontMetricsProvider: FontData
    ) : RenderAttributes
}