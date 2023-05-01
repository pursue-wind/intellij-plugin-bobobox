package io.github.pursuewind.intellij.plugin.inline

import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import io.github.pursuewind.intellij.plugin.inline.domain.EditorCallback

class MarkupModelListenerKt(private val editorCallback: EditorCallback) : MarkupModelListener {

    override fun afterAdded(highlighter: RangeHighlighterEx) {
        editorCallback.onAdded(highlighter)
    }

    override fun beforeRemoved(highlighter: RangeHighlighterEx) {
        editorCallback.onRemoved(highlighter)
    }

    override fun attributesChanged(
        highlighter: RangeHighlighterEx,
        renderersChanged: Boolean,
        fontStyleOrColorChanged: Boolean
    ) {
        beforeRemoved(highlighter)
        afterAdded(highlighter)
    }
}