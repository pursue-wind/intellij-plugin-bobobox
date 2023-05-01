package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import javax.swing.JPanel

class NumberOfWhitespacesComponent(numberOfWhitespaces: Int) : Component, State<Int?> {
    private val numberOfWhitespacesField: JBTextField

    init {
        numberOfWhitespacesField = JBTextField("" + numberOfWhitespaces)
    }

    override fun addToBuilder(formBuilder: FormBuilder) {
        val panel = JPanel().also {
            it.add(JBLabel(message("inline.setting.editor.line.whitespace")))
            it.add(numberOfWhitespacesField)
        }

        formBuilder.addLabeledComponent(panel, JBLabel())
    }

    override fun getState(): Int {
        val text = numberOfWhitespacesField.text
        var num = 0
        try {
            num = text.toInt()
        } catch (ignored: NumberFormatException) {
        }
        return num
    }
}
