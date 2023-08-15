package io.github.pursuewind.intellij.plugin.generate.increment

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import io.github.pursuewind.intellij.plugin.generate.MyEditorAction

/**
 * 行转列
 * eg:
 *      a,b,c      a,1,2
 *      1,2,3  ->  b,2,4
 *      2,4,6      c,3,6
 */
class Row2ColAction : MyEditorAction(null) {
    init {
        setupHandler(object : EditorWriteActionHandler(false) {
            override fun executeWriteAction(editor: Editor, caret: Caret?, dataContext: DataContext)  {
                val allCarets = editor.caretModel.allCarets
                val selectedTexts = allCarets.mapNotNull { it.selectedText }

                if (selectedTexts.isNotEmpty()) {
                    val lineCharMaps = selectedTexts
                        .map { line ->
                            line.filterNot { it in 'a'..'z' || it in 'A'..'Z' || it.isDigit() || it.isChineseCharacter() }
                                .groupingBy { it }
                                .eachCount()
                        }
                    val splitChar = lineCharMaps
                        .flatMap { it.keys }
                        .filter { char ->
                            // 对每个字符检查在所有映射中的出现次数是否相同
                            val firstCount = lineCharMaps[0][char]
                            lineCharMaps.all { it[char] == firstCount }
                        }.first { it == '\t' || it == ' ' || it == ',' || it == '|' }

                    val newText = selectedTexts
                        .map { it.split(splitChar.toString()) }
                        .transpose()
                        .joinToString("\n") { it.joinToString(splitChar.toString()) }

                    val start = allCarets.minOf { it.selectionStart }
                    val end = allCarets.maxOf { it.selectionEnd }
                    editor.document.replaceString(start, end, newText)

                    editor.selectionModel.setSelection(start, start + newText.length)
                }
            }
        })
    }

    fun Char.isChineseCharacter(): Boolean {
        return this.code in 0x4E00..0x9FFF
    }

    fun List<List<String>>.transpose(): List<List<String>> {
        return if (isEmpty()) emptyList() else {
            val width = first().size
            (0 until width).map { col -> map { it[col] } }
        }
    }

}
