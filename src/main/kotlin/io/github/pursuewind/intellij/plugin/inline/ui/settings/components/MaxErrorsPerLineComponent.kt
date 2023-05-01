package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.data.DefaultSettings
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import javax.swing.JPanel

class MaxErrorsPerLineComponent(maxErrorsPerLine: Int) : Component, State<Int?> {
    private val maxErrorsPerLineField: JBTextField

    init {
        maxErrorsPerLineField = JBTextField("" + maxErrorsPerLine)
    }

    override fun addToBuilder(formBuilder: FormBuilder) {
        val panel = JPanel()
        panel.add(JBLabel(message("inline.setting.editor.error.number.per_line")))
        panel.add(maxErrorsPerLineField)
        formBuilder.addLabeledComponent(panel, JBLabel())
    }

    override fun getState(): Int {
        val text = maxErrorsPerLineField.text
        var num = DefaultSettings.MAX_ERRORS_PER_LINE
        try {
            num = text.toInt()
        } catch (ignored: NumberFormatException) {
        }
        return num
    }
}
