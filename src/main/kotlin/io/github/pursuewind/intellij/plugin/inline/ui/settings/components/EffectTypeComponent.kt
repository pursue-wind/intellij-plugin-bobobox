package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.inline.data.EffectType
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import javax.swing.JLabel
import javax.swing.JPanel

class EffectTypeComponent(effectType: EffectType) : JPanel(), Component, State<EffectType> {
    private val effectTypeComboBox: ComboBox<String> = ComboBox(values)

    init {
        for (i in values.indices) {
            if (values[i] == effectType.name) effectTypeComboBox.selectedIndex = i
        }
    }

    override fun addToBuilder(formBuilder: FormBuilder) {
        val panel = JPanel()
        panel.add(JBLabel(message("inline.setting.editor.effect.style")))
        panel.add(effectTypeComboBox)
        formBuilder.addLabeledComponent(panel, JLabel())
    }

    override fun getState(): EffectType {
        val index = effectTypeComboBox.selectedIndex
        return EffectType.valueOf(values[index])
    }

    companion object {
        private val values = getValues()
        private fun getValues(): Array<String> {
            val effects = ArrayList<String>(5)
            for (type in EffectType.values()) {
                effects.add(type.name)
            }
            return effects.toTypedArray<String>()
        }
    }
}
