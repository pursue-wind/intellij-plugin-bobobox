package io.github.pursuewind.intellij.plugin.generate.increment

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import io.github.pursuewind.intellij.plugin.generate.MyEditorAction
import io.github.pursuewind.intellij.plugin.generate.increment.UniversalNumber.Companion.increment
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern

class IncrementAction(setupHandler: Boolean = true) : MyEditorAction(null) {
    init {
        if (setupHandler) {
            setupHandler(object : EditorWriteActionHandler(true) {
                override fun executeWriteAction(editor: Editor, caret: Caret?, dataContext: DataContext) {
//					MyApplicationService.setAction(getActionClass());

                    // Column mode not supported
                    if (editor.isColumnMode) {
                        return
                    }
                    val caretModel = editor.caretModel
                    val line = caretModel.logicalPosition.line
                    val column = caretModel.logicalPosition.column
                    val caretOffset = caretModel.offset
                    val selectionModel = editor.selectionModel
                    val hasSelection = selectionModel.hasSelection()
                    if (!hasSelection) {
                        selectionModel.selectLineAtCaret()
                    }
                    val selectedText = selectionModel.selectedText
                    if (selectedText != null) {
                        val newText = processSelection(selectedText)
                        applyChanges(
                            editor, caretModel, line, column, selectionModel, hasSelection, selectedText, newText,
                            caretOffset
                        )
                    }
                }
            })
        }
    }

    protected fun processSelection(selectedText: String): String {
        val textParts = splitPreserveAllTokens(selectedText, UniversalNumber.UNIVERSAL_NUMBER_REGEX)
        for (i in textParts.indices) {
            textParts[i] = increment(textParts[i])
        }
        return StringUtils.join(*textParts)
    }

    protected fun applyChanges(
        editor: Editor, caretModel: CaretModel?, line: Int, column: Int,
        selectionModel: SelectionModel, hasSelection: Boolean, selectedText: String?, newText: String?, caretOffset: Int
    ) {
        editor.document.replaceString(
            selectionModel.selectionStart, selectionModel.selectionEnd,
            newText!!
        )
    }

    companion object {
        /**
         *
         * Splits the given input sequence around matches of this pattern.
         *
         *
         *
         *
         *
         *  The array returned by this method contains each substring of the input sequence
         * that is terminated by another subsequence that matches this pattern or is terminated by
         * the end of the input sequence.
         * The substrings in the array are in the order in which they occur in the input.
         * If this pattern does not match any subsequence of the input then the resulting array
         * has just one element, namely the input sequence in string form.
         *
         *
         *
         *
         * <pre>
         * splitPreserveAllTokens("boo:and:foo", ":") =  { "boo", ":", "and", ":", "foo"}
         * splitPreserveAllTokens("boo:and:foo", "o") =  { "b", "o", "o", ":and:f", "o", "o"}
        </pre> *
         *
         * @param input The character sequence to be split
         * @return The array of strings computed by splitting the input around matches of this pattern
         */
        @JvmStatic
        fun splitPreserveAllTokens(input: String, regex: String?): Array<String> {
            var index = 0
            val p = Pattern.compile(regex)
            val result = ArrayList<String>()
            val m = p.matcher(input)

            // Add segments before each match found
            val lastBeforeIdx = 0
            while (m.find()) {
                if (StringUtils.isNotEmpty(m.group())) {
                    val match = input.subSequence(index, m.start()).toString()
                    if (StringUtils.isNotEmpty(match)) {
                        result.add(match)
                    }
                    result.add(input.subSequence(m.start(), m.end()).toString())
                    index = m.end()
                }
            }

            // If no match was found, return this
            if (index == 0) {
                return arrayOf(input)
            }
            val remaining = input.subSequence(index, input.length).toString()
            if (StringUtils.isNotEmpty(remaining)) {
                result.add(remaining)
            }

            // Construct result
            return result.toTypedArray<String>()
        }
    }
}