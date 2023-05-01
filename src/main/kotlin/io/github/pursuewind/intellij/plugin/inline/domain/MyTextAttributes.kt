package io.github.pursuewind.intellij.plugin.inline.domain

import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color
import java.awt.Font

class MyTextAttributes(
    backgroundColor: Color?
) : TextAttributes(null, backgroundColor, null, null, Font.PLAIN) {
    companion object {
        val EMPTY = MyTextAttributes(null)
    }
}
