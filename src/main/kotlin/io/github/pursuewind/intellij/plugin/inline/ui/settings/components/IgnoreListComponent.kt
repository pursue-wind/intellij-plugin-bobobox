package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.ui.components.JBTextArea
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State

class IgnoreListComponent(ignoreList: Array<String>) :  State<Array<String>> {
    var textComponent: JBTextArea= JBTextArea(ignoreList.joinToString("\n"))


    override fun getState(): Array<String> {
        val fulltext = textComponent.text
        val split = fulltext.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return split.filter { line: String -> line.isNotBlank() }.toTypedArray()
    }

}
