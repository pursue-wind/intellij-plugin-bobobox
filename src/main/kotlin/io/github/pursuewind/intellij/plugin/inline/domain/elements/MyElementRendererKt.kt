package io.github.pursuewind.intellij.plugin.inline.domain.elements

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import io.github.pursuewind.intellij.plugin.inline.data.FontData
import io.github.pursuewind.intellij.plugin.inline.domain.NumberOfWhitespaces
import io.github.pursuewind.intellij.plugin.inline.domain.graphics.GraphicsComponentKt
import java.awt.Graphics
import java.awt.Rectangle

class MyElementRendererKt(
    private val graphicsComponents: Collection<GraphicsComponentKt>,
    private val numberOfWhitespaces: NumberOfWhitespaces,
    private val editorFontMetricsProvider: FontData,
) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        graphicsComponents.sumOf(GraphicsComponentKt::width)

    override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
        val charWidth = editorFontMetricsProvider.fontMetrics.charWidth('a')
        val newTargetRegion = Rectangle(targetRegion.x + charWidth * numberOfWhitespaces.numberOfWhitespaces, targetRegion.y, targetRegion.width, targetRegion.height)
        graphicsComponents.asSequence()
            .sortedBy(GraphicsComponentKt::priority)
            .forEach { it.draw(g, newTargetRegion) }
    }
}