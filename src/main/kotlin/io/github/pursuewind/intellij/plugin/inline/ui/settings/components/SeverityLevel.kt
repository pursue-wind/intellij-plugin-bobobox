package io.github.pursuewind.intellij.plugin.inline.ui.settings.components

import com.intellij.ui.ColorPanel
import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import io.github.pursuewind.intellij.plugin.inline.data.SeverityLevelState
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.ui.refactor.SeverityLevelConf
import io.github.pursuewind.intellij.plugin.inline.ui.settings.Component
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class SeverityLevel(
    data: SeverityLevelState, private val name: String, private val helpDescription: String
) : State<SeverityLevelState> {
    private val textColorPanel: ColorPanel
    private val backgroundColorPanel: ColorPanel
    private val effectColorPanel: ColorPanel
    private val showTextCheckBox: JBCheckBox
    private val showBackgroundCheckBox: JBCheckBox
    private val showEffectCheckBox: JBCheckBox
    private val showGutterIcon: JBCheckBox

    init {
        showGutterIcon = JBCheckBox(message("inline.setting.editor.show.icon"), data.showGutterIcon)
        textColorPanel = ColorPanel()
        textColorPanel.selectedColor = data.textColor
        backgroundColorPanel = ColorPanel()
        backgroundColorPanel.selectedColor = data.backgroundColor
        effectColorPanel = ColorPanel()
        effectColorPanel.selectedColor = data.effectColor
        showTextCheckBox = JBCheckBox(message("inline.setting.editor.text.color"), data.showText)
        showBackgroundCheckBox = JBCheckBox(message("inline.setting.editor.background.color"), data.showBackground)
        showEffectCheckBox = JBCheckBox(message("inline.setting.editor.effect.color"), data.showEffect)
    }

    fun buildJPanel(): JPanel {
        val panel = JPanel().also {
            it.add(JBLabel(name + message("inline.setting.editor.settings") + ": "))
            it.add(ContextHelpLabel.create(helpDescription))
        }

        val panel1 = JPanel().also {
            it.add(showGutterIcon)
        }
        val panel2 = JPanel().also {
            it.add(showTextCheckBox)
            it.add(textColorPanel)
        }
        val panel3 = JPanel().also {
            it.add(showBackgroundCheckBox)
            it.add(backgroundColorPanel)
        }
        val panel4 = JPanel().also {
            it.add(showEffectCheckBox)
            it.add(effectColorPanel)
        }
        val contentPanel = JPanel(GridBagLayout()).also {
            val gbc = GridBagConstraints()

            it.add(panel, gbc)
            it.add(panel1, gbc)
            it.add(panel2, gbc)
            it.add(panel3, gbc)
            it.add(panel4, gbc)
        }

        return contentPanel
    }

    override fun getState(): SeverityLevelState {
        return SeverityLevelState(
            showGutterIcon.isSelected,
            showTextCheckBox.isSelected,
            showBackgroundCheckBox.isSelected,
            showEffectCheckBox.isSelected,
            textColorPanel.selectedColor!!,
            backgroundColorPanel.selectedColor!!,
            effectColorPanel.selectedColor!!
        )
    }
}
