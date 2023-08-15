package io.github.pursuewind.intellij.plugin.generate.increment

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.find.FindResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.actions.EditorActionUtil
import com.intellij.openapi.editor.actions.IncrementalFindAction
import com.intellij.openapi.editor.actions.SelectOccurrencesActionHandler
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.util.Pair
import io.github.pursuewind.intellij.plugin.generate.MyEditorAction

object SelectAllWithRegAction : MyEditorAction(Handler()) {
    private fun getCaretPosition(findResult: FindResult, caretShiftFromSelectionStart: Int): Int {
        return if (caretShiftFromSelectionStart < 0)
            findResult.endOffset
        else Math.min(
            findResult.startOffset + caretShiftFromSelectionStart,
            findResult.endOffset
        )
    }

    class Handler : SelectOccurrencesActionHandler() {
        public override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext): Boolean {
            return (editor.project != null && editor.caretModel.supportsMultipleCarets()
                    && !IncrementalFindAction.SEARCH_DISABLED[editor, false])
        }

        public override fun doExecute(editor: Editor, c: Caret?, dataContext: DataContext) {
            val selectedTexts = HashSet<Pair<String, Boolean>>()
            val models: MutableList<MyModel> = ArrayList()
            val allCarets = editor.caretModel.allCarets
            for (caret in allCarets) {
                val wholeWordsSearch =
                    !caret.hasSelection() && getSelectionRange(editor, caret)?.let { wordSelectionRange ->
                        setSelection(editor, caret, wordSelectionRange)
                        true
                    } ?: false
                var regSearch = false
                val selectedText = caret.selectedText?.let {
                    if (it.startsWith("//")) {
                        regSearch = true
                        it.substring(2)
                    } else {
                        it
                    }
                }

                val project = editor.project
                if (project == null || selectedText == null) {
                    continue
                }

                val pair = Pair.create(selectedText, wholeWordsSearch)
                if (selectedTexts.contains(pair)) {
                    continue
                } else {
                    selectedTexts.add(pair)
                }
                val caretShiftFromSelectionStart = caret.offset - caret.selectionStart
                val findManager = FindManager.getInstance(project)

                val model = FindModel().apply {
                    stringToFind = selectedText
                    isCaseSensitive = true
                    isWholeWordsOnly = wholeWordsSearch
                    isRegularExpressions = regSearch
                }

                models.add(MyModel(editor, caretShiftFromSelectionStart, findManager, model))
            }
            selectSearchResultsInEditor(editor, models)
            editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
        }

        private fun selectSearchResultsInEditor(editor: Editor, models: List<MyModel>) {
            val caretStates = editor.caretModel.caretsAndSelections
            for (model in models) {
                val resultIterator: Iterator<FindResult> = FindResultIterator(model)
                while (resultIterator.hasNext()) {
                    val findResult = resultIterator.next()
                    val caretOffset = getCaretPosition(findResult, model.caretShiftFromSelectionStart)
                    val selectionStartOffset = findResult.startOffset
                    val selectionEndOffset = findResult.endOffset
                    EditorActionUtil.makePositionVisible(editor, caretOffset)
                    EditorActionUtil.makePositionVisible(editor, selectionStartOffset)
                    EditorActionUtil.makePositionVisible(editor, selectionEndOffset)
                    caretStates.add(
                        CaretState(
                            editor.offsetToLogicalPosition(caretOffset),
                            editor.offsetToLogicalPosition(selectionStartOffset),
                            editor.offsetToLogicalPosition(selectionEndOffset)
                        )
                    )
                }
                try {
                    if (caretStates.size > editor.caretModel.maxCaretCount) {
                        EditorUtil.notifyMaxCarets(editor)
                        return
                    }
                } catch (e: Throwable) {
                    //old api
                }
                if (caretStates.isNotEmpty()) {
                    editor.caretModel.caretsAndSelections = caretStates
                }
            }
        }
    }

    private class FindResultIterator(private val myModel: MyModel) : MutableIterator<FindResult> {
        var findResult = myModel.findManager.findString(myModel.editor.document.charsSequence, 0, myModel.model)

        override fun hasNext(): Boolean {
            return findResult.isStringFound
        }

        override fun next(): FindResult {
            val result = findResult
            findResult = myModel.findManager.findString(
                myModel.editor.document.charsSequence,
                findResult.endOffset,
                myModel.model
            )
            return result
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }
    }

    class MyModel(
        val editor: Editor,
        val caretShiftFromSelectionStart: Int,
        val findManager: FindManager,
        val model: FindModel
    )
}
