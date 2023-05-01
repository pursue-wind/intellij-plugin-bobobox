package io.github.pursuewind.intellij.plugin.inline.ui.settings

import com.intellij.ui.FontComboBox
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import io.github.pursuewind.intellij.plugin.generate.message
import io.github.pursuewind.intellij.plugin.inline.data.SettingsState
import io.github.pursuewind.intellij.plugin.inline.ui.UI
import io.github.pursuewind.intellij.plugin.inline.ui.UI.emptyBorder
import io.github.pursuewind.intellij.plugin.inline.ui.UI.migLayoutVertical
import io.github.pursuewind.intellij.plugin.inline.ui.UI.plus
import io.github.pursuewind.intellij.plugin.inline.ui.UI.wrap
import io.github.pursuewind.intellij.plugin.inline.ui.refactor.SettingsUi
import io.github.pursuewind.intellij.plugin.inline.ui.settings.components.*
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.text.JTextComponent

interface SettingsComponentProvider : State<SettingsState> {
    fun createComponent(): JComponent
    override fun getState(): SettingsState

    class Main(settingsState: SettingsState) : SettingsComponentProvider {


        protected val wholePanel: JPanel = JPanel()
        val p = """
                |Hope is the thing with feathers
                |That perches in the soul
                |And sings the tune without the words
                |And never stops """.trimMargin()

        protected val primaryFontPreview: JTextComponent = JEditorPane(
            "text/plain",
            message("inline.setting.font.default.preview.text") + "\n" + p
        )

        private fun JComponent.previewFont(primary: String?) {
            font = if (primary.isNullOrBlank()) {
                UI.defaultFont
            } else {
                JBUI.Fonts.create(primary, UIUtil.getFontSize(UIUtil.FontSize.NORMAL).toInt())
            }
        }

        protected val restoreDefaultButton = JButton(message("inline.setting.button.restore.default"))

        private var numberOfWhitespacesComponent = NumberOfWhitespacesComponent(settingsState.numberOfWhitespaces)
        private var maxErrorsPerLineComponent = MaxErrorsPerLineComponent(settingsState.maxErrorsPerLine)
        private var effectTypeComponent = EffectTypeComponent(settingsState.effectType)
        private var fontSelectionComponent = FontComboBox(false, false, false)
        private var oneGutterModeComponent = OneGutterModeComponent(settingsState.oneGutterMode)
        private var textStyleSelectionComponent = TextStyleSelectionComponent(settingsState.textStyle)
        private var errorComponent = SeverityLevel(
            settingsState.error,
            message("inline.setting.editor.error.name"),
            message("inline.setting.editor.error.desc")
        )
        private var warningComponent = SeverityLevel(
            settingsState.warning,
            message("inline.setting.editor.warn.name"),
            message("inline.setting.editor.warn.desc")
        )
        private var weakWarningComponent = SeverityLevel(
            settingsState.weakWarning,
            message("inline.setting.editor.weak_warn.name"),
            message("inline.setting.editor.weak_warn.desc")
        )
        private var informationComponent = SeverityLevel(
            settingsState.information,
            message("inline.setting.editor.info.name"),
            message("inline.setting.editor.info.desc")
        )
        private var serverErrorComponent = SeverityLevel(
            settingsState.serverError,
            message("inline.setting.editor.server_error.name"),
            message("inline.setting.editor.server_error.desc")
        )
        private var otherErrorComponent = SeverityLevel(
            settingsState.otherError,
            message("inline.setting.editor.other.name"),
            message("inline.setting.editor.other.desc")
        )
        private var ignoreListComponent = IgnoreListComponent(settingsState.ignoreList)

        private val warnPanel = titledPanel(message("inline.setting.editor.display.setting")) {
            add(errorComponent.buildJPanel(), wrap())
            add(warningComponent.buildJPanel(), wrap())
            add(weakWarningComponent.buildJPanel(), wrap())
            add(informationComponent.buildJPanel(), wrap())
            add(serverErrorComponent.buildJPanel(), wrap())
            add(otherErrorComponent.buildJPanel(), wrap())
        }
        private val ignoreListPanel = titledPanel(message("inline.setting.panel.ignore")) {
            add(ignoreListComponent.textComponent, wrap())
        }

        override fun createComponent(): JComponent {
            setListeners()
            val formBuilder = FormBuilder.createFormBuilder()

            formBuilder.addSeparator()
            textStyleSelectionComponent.addToBuilder(formBuilder)
            effectTypeComponent.addToBuilder(formBuilder)
            formBuilder.addSeparator()
            oneGutterModeComponent.addToBuilder(formBuilder)
            formBuilder.addSeparator()
            maxErrorsPerLineComponent.addToBuilder(formBuilder)
            numberOfWhitespacesComponent.addToBuilder(formBuilder)

            wholePanel.addVertically(
                fontsPanel,
                formBuilder.panel,
                warnPanel,
                ignoreListPanel
            )

            return wholePanel
        }

        override fun getState(): SettingsState {
            val state = SettingsState()
            state.numberOfWhitespaces = numberOfWhitespacesComponent.getState()
            state.effectType = effectTypeComponent.getState()
            state.maxErrorsPerLine = maxErrorsPerLineComponent.getState()
            state.font = fontSelectionComponent.fontName ?: "Dialog.plain"
            state.textStyle = textStyleSelectionComponent.getState()
            state.oneGutterMode = oneGutterModeComponent.getState()
            state.error = errorComponent.getState()
            state.warning = warningComponent.getState()
            state.weakWarning = weakWarningComponent.getState()
            state.information = informationComponent.getState()
            state.serverError = serverErrorComponent.getState()
            state.otherError = otherErrorComponent.getState()
            state.ignoreList = ignoreListComponent.getState()
            return state
        }

        private fun JPanel.addVertically(vararg components: JComponent) {
            layout = migLayoutVertical()
            components.forEach {
                add(it, fillX())
            }
            add(JPanel(), fillY())
        }


        private fun setListeners() {
            fontSelectionComponent.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    primaryFontPreview.previewFont(fontSelectionComponent.fontName)
                }
            }
            restoreDefaultButton.addActionListener {
                fontSelectionComponent.fontName = null
                primaryFontPreview.previewFont(null)
            }
        }


        private val fontsPanel = titledPanel(message("inline.setting.panel.title.fonts.setting")) {
            add(JPanel().also {
                it.add(JLabel(message("inline.setting.panel.title.fonts")))
                it.add(fontSelectionComponent)
            }, wrap())

            add(primaryFontPreview, fillX().wrap())


            add(restoreDefaultButton)

            //compensate custom border of ComboBox
            primaryFontPreview.border = emptyBorder(3) + JBUI.Borders.customLine(JBColor.border())
        }

        private fun titledPanel(title: String, fill: Boolean = true, body: JPanel.() -> Unit): JComponent {
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

        fun migLayout(gapX: String = "0!", gapY: String = "0!", insets: String = "0") =
            MigLayout(LC().fill().gridGap(gapX, gapY).insets(insets))

        fun fill(): CC = CC().grow().push()
        fun fillX(): CC = CC().growX().pushX()
        fun fillY(): CC = CC().growY().pushY()


    }


}
