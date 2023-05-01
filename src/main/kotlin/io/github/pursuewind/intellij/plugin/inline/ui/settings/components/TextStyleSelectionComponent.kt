package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.inline.domain.TextStyle
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import javax.swing.JLabel
import javax.swing.JPanel

class TextStyleSelectionComponent(textStyle: TextStyle) : Component, State<TextStyle?> {
    private val stylesComboBox: ComboBox<String> = ComboBox(values)

    init {
        for (i in values.indices) if (values[i] == textStyle.name) stylesComboBox.selectedIndex = i
    }

    override fun addToBuilder(formBuilder: FormBuilder) {
        val panel = JPanel()
        panel.add(JLabel(message("inline.setting.editor.text.style")))
        panel.add(stylesComboBox)
        formBuilder.addLabeledComponent(panel, JLabel())
    }

    override fun getState(): TextStyle {
        val index = stylesComboBox.selectedIndex
        return TextStyle.valueOf(values[index])
    }

    companion object {
        private val values = getValues()
        private fun getValues(): Array<String> {
            val styles = ArrayList<String>(2)
            for (style in TextStyle.values()) {
                styles.add(style.name)
            }
            return styles.toTypedArray<String>()
        }
    }
}
