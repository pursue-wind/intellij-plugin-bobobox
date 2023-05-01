package io.github.pursuewind.intellij.plugin.inline.ui.refactor


import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.EditorTextField
import com.intellij.ui.FontComboBox
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import io.github.pursuewind.intellij.plugin.inline.ui.UI
import org.intellij.lang.regexp.RegExpLanguage
import javax.swing.JComponent

/**
 * SettingsPanel
 */
class SettingsPanel(
    private val settings: Settings, override val preferredFocusedComponent: JComponent?
) : SettingsUi(), ConfigurableUi {

    private var validRegExp = true

    override val component: JComponent = wholePanel

    init {
        primaryFontComboBox.fixFontComboBoxSize()
        doLayout(settings)
        setListeners()
    }




    private fun setListeners() {

    }

    private fun JComponent.previewFont(primary: String?) {
        font = if (primary.isNullOrBlank()) {
            UI.defaultFont
        } else {
            JBUI.Fonts.create(primary, UIUtil.getFontSize(UIUtil.FontSize.NORMAL).toInt())
        }
    }






    override val isModified: Boolean
        get() {
            if (!validRegExp) {
                return false
            }

            val settings = settings
            //todo
            return true
        }


    override fun apply() {


        @Suppress("Duplicates")
        with(settings) {



        }
    }

    @Suppress("Duplicates")
    override fun reset() {

    }

    companion object {
        private val BACKGROUND_COLOR_ERROR = JBColor(0xffb1a0, 0x6e2b28)

        private fun FontComboBox.fixFontComboBoxSize() {
            val size = preferredSize
            size.width = size.height * 8
            preferredSize = size
        }
    }
}