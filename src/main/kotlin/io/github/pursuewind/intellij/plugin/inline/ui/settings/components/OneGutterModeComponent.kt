package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import javax.swing.JLabel
import javax.swing.JPanel

class OneGutterModeComponent(oneGutterMode: Boolean) : Component, State<Boolean> {
    private val checkBox: JBCheckBox = JBCheckBox()

    init {
        checkBox.isSelected = oneGutterMode
    }

    override fun addToBuilder(formBuilder: FormBuilder) {
        val panel = JPanel().also {
            it.add(JLabel(message("inline.setting.editor.show.icon_mode")))
            val tip = message("inline.setting.editor.show.icon_mode_tip")
            it.add(ContextHelpLabel.create(tip))
            it.add(checkBox)
        }
        formBuilder.addLabeledComponent(panel, JLabel())
    }

    override fun getState(): Boolean {
        return checkBox.isSelected
    }
}
