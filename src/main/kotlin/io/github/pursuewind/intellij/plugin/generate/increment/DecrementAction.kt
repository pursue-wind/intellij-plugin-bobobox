package io.github.pursuewind.intellij.plugin.generate.increment

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import io.github.pursuewind.intellij.plugin.generate.MyEditorAction
import io.github.pursuewind.intellij.plugin.generate.increment.IncrementAction.Companion.splitPreserveAllTokens
import io.github.pursuewind.intellij.plugin.generate.increment.UniversalNumber.Companion.decrement
import org.apache.commons.lang3.StringUtils

class DecrementAction (setupHandler: Boolean = true) : MyEditorAction(null) {
    init {
        if (setupHandler) {
            setupHandler(object : EditorWriteActionHandler(true) {
                override fun executeWriteAction(editor: Editor, dataContext: DataContext) {

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
                    if (hasSelection == false) {
                        selectionModel.selectLineAtCaret()
                    }
                    val selectedText = selectionModel.selectedText
                    if (selectedText != null) {
                        val newText = processSelection(selectedText)
                        applyChanges(
                            editor,
                            caretModel,
                            line,
                            column,
                            selectionModel,
                            hasSelection,
                            newText,
                            caretOffset
                        )
                    }
                }
            })
        }
    }

    protected fun processSelection(selectedText: String): String {
        val textParts = splitPreserveAllTokens(
            selectedText, UniversalNumber.UNIVERSAL_NUMBER_REGEX
        )
        for (i in textParts.indices) {
            textParts[i] = decrement(textParts[i])
        }
        return StringUtils.join(*textParts)
    }

    protected fun applyChanges(
        editor: Editor, caretModel: CaretModel?, line: Int, column: Int,
        selectionModel: SelectionModel, hasSelection: Boolean, newText: String?, caretOffset: Int
    ) {
        editor.document.replaceString(
            selectionModel.selectionStart, selectionModel.selectionEnd,
            newText!!
        )
    }
}
