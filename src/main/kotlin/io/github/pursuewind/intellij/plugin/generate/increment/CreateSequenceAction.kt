package io.github.pursuewind.intellij.plugin.generate.increment

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import io.github.pursuewind.intellij.plugin.generate.MyEditorAction
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import java.util.concurrent.atomic.AtomicReference
import java.util.regex.Pattern

class CreateSequenceAction(setupHandler: Boolean = true) : MyEditorAction(null) {
    init {
        if (setupHandler) {
            setupHandler(object : EditorWriteActionHandler(false) {
                override fun executeWriteAction(editor: Editor, dataContext: DataContext) {
                    val lastValue = AtomicReference<String>()
                    editor.caretModel.runForEachCaret { caret ->
                        if (caret.isValid) {
                            val newText = processSelection(caret, lastValue)
                            editor.document.replaceString(caret.selectionStart, caret.selectionEnd, newText)
                            editor.selectionModel.setSelection(
                                caret.selectionStart,
                                caret.selectionStart + newText.length
                            )
                        }
                    }
                }
            })
        }
    }

    private fun processSelection(caret: Caret, lastValue: AtomicReference<String>): String {
        val selectedText = caret.selectedText ?: "0"
        val textParts = splitPreserveAllTokens(selectedText, UniversalNumber.UNIVERSAL_NUMBER_REGEX)
        textParts.forEachIndexed { i, textPart ->
            textParts[i] = processTextPart(lastValue, textPart)
        }
        return textParts.joinToString("")
    }

    private fun processTextPart(lastValue: AtomicReference<String>, textPart: String): String {
        var s = textPart


        if (A.strEnumMap[s] != null|| A.strEnumMap[lastValue.get()] != null) {
            val last = lastValue.get()
            if (last != null) {
                s = A.findNext(last)
            }
            lastValue.set(s)
        }

        if (getNumber(s) != null) {
            val last = lastValue.get()
            if (last != null) {
                s = UniversalNumber.increment(last)
            }
            lastValue.set(s)
        }
        return s
    }

    companion object {
        fun getNumber(str: String?): Number? {
            var number: Number? = null
            try {
                number = NumberUtils.createNumber(str)
            } catch (e: NumberFormatException) {
                //Not a number, ignore it, will try to create a double
            } catch (e: IndexOutOfBoundsException) {
                //Bug in createNumber, ignore it, will try to create a double
            }
            if (number == null) {
                try {
                    number = NumberUtils.createDouble(str)
                } catch (e1: Exception) {
                    //nothing to do, really not a number or too complex for NumberUtils
                }
            }
            return number
        }

        fun splitPreserveAllTokens(input: String, regex: String): Array<String> {
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