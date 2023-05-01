package io.github.pursuewind.intellij.plugin.inline.domain

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Disposer
import io.github.pursuewind.intellij.plugin.inline.domain.elements.RenderElementKt
import io.github.pursuewind.intellij.plugin.inline.domain.wrapper.RangeHighlighterWrapper
import io.github.pursuewind.intellij.plugin.inline.domain.wrapper.WrapperComparator

interface ViewModel {

    fun add(highlighter: RangeHighlighterWrapper)

    fun remove(highlighter: RangeHighlighterWrapper)

    class Impl(
        private val renderElementsProvider: RenderElementsProvider,
        private val editor: Editor,
        private val maxPerLine: MaxErrorsPerLine,
        private val highlightersValidator: HighlightersValidator,
    ) : ViewModel {

        private val map = HashMap<RangeHighlighterWrapper, List<Disposable>>()

        override fun add(highlighter: RangeHighlighterWrapper) {

            hideFromMapAndRemoveInvalid()

            val line = highlighter.lineNumber
            if (line == -1) return

            map[highlighter] = emptyList()

            displayHighlightersOnCurrentLineAndUpdateMap(line)
        }

        override fun remove(highlighter: RangeHighlighterWrapper) {
            val dis = map.remove(highlighter) ?: return
            dis.forEach(Disposable::dispose)

            val line = highlighter.lineNumber
            if (line == -1) return

            hideFromMapAndRemoveInvalid()

            displayHighlightersOnCurrentLineAndUpdateMap(line)
        }

        private fun displayHighlightersOnCurrentLineAndUpdateMap(currentLine: Int) {

            val lineStartOffset = editor.document.getLineStartOffset(currentLine)

            val highlightersOnCurrentLine = map.keys.asSequence()
                .filter { it.lineNumber == currentLine }
                .toList()

            highlightersOnCurrentLine.forEach {
                val disposables = map[it] ?: emptyList()
                disposables.forEach(Disposer::dispose)
                map[it] = emptyList()
            }

            val topN = highlightersOnCurrentLine.asSequence()
                .sortedWith(PRIORITY_LAST_DESC)
                .filter { highlightersValidator.isValid(it) }
                .take(maxPerLine.maxPerLine)
                .toList()

            val lineRenderElementsSortedByPriority: Map<RangeHighlighterWrapper, Collection<RenderElementKt>> =
                renderElementsProvider.provide(lineStartOffset, topN)

            val renderElementsFromRightToLeft = lineRenderElementsSortedByPriority.asSequence()
                .sortedWith(OFFSET_FROM_RIGHT_TO_LEFT)
                .map { it.value }
                .toList()

            for (i in renderElementsFromRightToLeft.indices) {
                map[topN[i]] = renderElementsFromRightToLeft[i].map { it.render(editor) }
            }

        }

        private fun hideFromMapAndRemoveInvalid() {

            map.keys
                .filter { !it.isValidInDocument() }
                .forEach {
                    val correspondingDisposables = map.remove(it) ?: emptyList()
                    correspondingDisposables.forEach(Disposer::dispose)
                }
        }

        private companion object {

            val PRIORITY_LAST_DESC =
                WrapperComparator.ByPriority then
                        WrapperComparator.ByOffsetTakeLastOnTheLine then
                        WrapperComparator.ByDescription

            val OFFSET_FROM_RIGHT_TO_LEFT = Comparator<Map.Entry<RangeHighlighterWrapper, *>> { h1, h2 ->
                WrapperComparator.ByOffsetTakeLastOnTheLine.compare(h1.key, h2.key)
            }
        }
    }
}