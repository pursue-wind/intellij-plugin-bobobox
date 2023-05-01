package io.github.pursuewind.intellij.plugin.inline.ui.refactor

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.panel.ComponentPanelBuilder
import com.intellij.ui.*
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.data.EffectType
import io.github.pursuewind.intellij.plugin.inline.data.SeverityLevelState
import io.github.pursuewind.intellij.plugin.inline.domain.TextStyle
import io.github.pursuewind.intellij.plugin.inline.ui.UI.emptyBorder
import io.github.pursuewind.intellij.plugin.inline.ui.UI.fill
import io.github.pursuewind.intellij.plugin.inline.ui.UI.fillX
import io.github.pursuewind.intellij.plugin.inline.ui.UI.fillY
import io.github.pursuewind.intellij.plugin.inline.ui.UI.migLayout
import io.github.pursuewind.intellij.plugin.inline.ui.UI.migLayoutVertical
import io.github.pursuewind.intellij.plugin.inline.ui.UI.plus
import io.github.pursuewind.intellij.plugin.inline.ui.UI.wrap
import io.github.pursuewind.intellij.plugin.inline.ui.settings.State
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.text.JTextComponent

class SeverityLevelConf(
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
            it.add(JBLabel(PREFIX + name + message("inline.setting.editor.settings") + ": "))
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

    companion object {
        private const val PREFIX = " "
    }
}

abstract class SettingsUi {
    protected val wholePanel: JPanel = JPanel()


    protected var primaryFontComboBox: FontComboBox = createFontComboBox(filterNonLatin = false)

    protected val primaryFontPreview: JTextComponent = JEditorPane(
        "text/plain",
        message("inline.setting.font.default.preview.text")
    )

    protected val phoneticFontPreview: JLabel = ComponentPanelBuilder.createCommentComponent(
        PHONETIC_CHARACTERS, true, 50, true
    )
    protected val restoreDefaultButton = JButton(message("inline.setting.button.restore.default"))


    protected fun doLayout(settings: Settings) {

        val fontsPanel = titledPanel(message("inline.setting.panel.title.fonts")) {
            add(JLabel(message("inline.setting.font.label.primary")), wrap())
            add(primaryFontComboBox, wrap())

            val primaryPreviewPanel = JPanel(migLayout()).apply {
                add(primaryFontPreview, fill())
            }
            val phoneticPreviewPanel = JPanel(migLayout()).apply {
                add(phoneticFontPreview, fill())
            }
            add(primaryPreviewPanel, fill())
            add(phoneticPreviewPanel, fill().wrap())

            add(restoreDefaultButton)

            //compensate custom border of ComboBox
            primaryFontPreview.border = emptyBorder(3) + JBUI.Borders.customLine(JBColor.border())
            primaryPreviewPanel.border = emptyBorder(3)
            phoneticFontPreview.border = emptyBorder(6)
        }


        //文本风格
        val textStyleComboBox = comboBox<TextStyle>().apply {
            renderer = SimpleListCellRenderer.create { label, value, _ ->
                label.text = value.name
            }
            addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
//                    fixEngineConfigurationComponent() //todo
                    println("Please select")
                }
            }
        }

        //效果类型
        val effectTypeComboBox = comboBox<EffectType>().apply {
            renderer = SimpleListCellRenderer.create { label, value, _ ->
                label.text = value.name
            }
            addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
//                    fixEngineConfigurationComponent() //todo
                    println("Please select")
                }
            }
        }
        val oneGutterModeCheckBox = JBCheckBox(message("inline.setting.editor.show.icon_mode"))


        val maxErrorsPerLineComboBox: ComboBox<Int> = comboBox((0..5).toList()).apply {
            isEditable = true
        }
        val numberOfWhitespacesComboBox: ComboBox<Int> = comboBox((0..20 step 2).toList()).apply {
            isEditable = true
        }

        val errorComponent = SeverityLevelConf(
            settings.error,
            message("inline.setting.editor.error.name"),
            message("inline.setting.editor.error.desc")
        ).buildJPanel()
        val warningComponent = SeverityLevelConf(
            settings.warning,
            message("inline.setting.editor.warn.name"),
            message("inline.setting.editor.warn.desc")
        ).buildJPanel()
        val weakWarningComponent = SeverityLevelConf(
            settings.weakWarning,
            message("inline.setting.editor.weak_warn.name"),
            message("inline.setting.editor.weak_warn.desc")
        ).buildJPanel()
        val informationComponent = SeverityLevelConf(
            settings.information,
            message("inline.setting.editor.info.name"),
            message("inline.setting.editor.info.desc")
        ).buildJPanel()
        val serverErrorComponent = SeverityLevelConf(
            settings.serverError,
            message("inline.setting.editor.server_error.name"),
            message("inline.setting.editor.server_error.desc")
        ).buildJPanel()
        val otherErrorComponent = SeverityLevelConf(
            settings.otherError,
            message("inline.setting.editor.other.name"),
            message("inline.setting.editor.other.desc")
        ).buildJPanel()

        val textStyleComboBoxPanel = titledPanel(message("inline.setting.editor.text.style")) {
            add(textStyleComboBox, wrap().span(2))
        }
        val effectTypeComboBoxPanel = titledPanel(message("inline.setting.editor.effect.style")) {
            add(effectTypeComboBox, wrap().span(2))
        }
        val oneGutterModeCheckBoxPanel = titledPanel(message("inline.setting.editor.show.icon_mode")) {
            add(oneGutterModeCheckBox, wrap().span(2))
        }

        val maxErrorsPerLineComboBoxPanel = titledPanel(message("inline.setting.editor.error.number.per_line")) {
            add(maxErrorsPerLineComboBox, wrap().span(2))
        }
        val numberOfWhitespacesComboBoxPanel = titledPanel(message("inline.setting.editor.line.whitespace")) {
            add(numberOfWhitespacesComboBox, wrap().span(2))
        }
        val warnPanel = titledPanel(message("inline.setting.editor.display.setting")) {
            add(errorComponent, wrap().span(2))
            add(warningComponent, wrap().span(2))
            add(weakWarningComponent, wrap().span(2))
            add(informationComponent, wrap().span(2))
            add(serverErrorComponent, wrap().span(2))
            add(otherErrorComponent, wrap().span(2))
        }

        wholePanel.addVertically(
            fontsPanel,
            textStyleComboBoxPanel,
            effectTypeComboBoxPanel,
            oneGutterModeCheckBoxPanel,
            maxErrorsPerLineComboBoxPanel,
            numberOfWhitespacesComboBoxPanel,
            warnPanel
        )
    }

    fun createMainPanel(settings: Settings): JPanel {
        doLayout(settings)
        return wholePanel
    }


    companion object {
        private const val PHONETIC_CHARACTERS =
            "iː əː ɔː uː aː i ə ɔ u æ e ʌ aɪ eɪ ɔɪ ɪə ɛə uə əu au p t k f s θ ʃ tʃ tr ts b d g v z ð ʒ dʒ dr dz m n ŋ h r l w j"

        private const val MIN_ELEMENT_WIDTH = 80

        private val ERROR_FOREGROUND_COLOR = UIUtil.getErrorForeground()

        private fun setMinWidth(component: JComponent, minWidth: Int = JBUIScale.scale(MIN_ELEMENT_WIDTH)) =
            component.apply {
                minimumSize = Dimension(minWidth, height)
            }

        private fun createFontComboBox(filterNonLatin: Boolean): FontComboBox =
            FontComboBox(false, filterNonLatin, false)

        private fun titledPanel(title: String, fill: Boolean = false, body: JPanel.() -> Unit): JComponent {
            val innerPanel = JPanel(migLayout(JBUIScale.scale(4).toString()))
            innerPanel.body()
            return JPanel(migLayout()).apply {
                border = IdeBorderFactory.createTitledBorder(title)
                if (fill) {
                    add(innerPanel, fillX())
                } else {
                    add(innerPanel)
                    add(JPanel(), fillX())
                }
            }
        }

        private fun JPanel.addVertically(vararg components: JComponent) {
            layout = migLayoutVertical()
            components.forEach {
                add(it, fillX())
            }
            add(JPanel(), fillY())
        }

        private fun <T> comboBox(elements: List<T>): ComboBox<T> = ComboBox(CollectionComboBoxModel(elements))

        private fun <T> comboBox(vararg elements: T): ComboBox<T> = comboBox(elements.toList())

        private inline fun <reified T : Enum<T>> comboBox(): ComboBox<T> = comboBox(enumValues<T>().toList())
    }
}