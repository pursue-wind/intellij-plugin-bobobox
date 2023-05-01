package io.github.pursuewind.intellij.plugin.inline.ui.refactor


import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ClearableLazyValue
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

/**
 * 选项配置
 */
class SettingsConfigurable : SearchableConfigurable {

    private val ui: ClearableLazyValue<ConfigurableUi> = ClearableLazyValue.create {
        SettingsPanel(Settings.instance,null)
    }

    override fun getId(): String = "xx"

    override fun getDisplayName(): String = "ShitBox Settings"

    override fun getHelpTopic(): String = "test"

    override fun createComponent(): JComponent = ui.value.component

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun isModified(): Boolean = ui.value.isModified

    override fun apply() {
        ui.value.apply()
    }

    override fun reset() {
        ui.value.reset()
    }

    override fun disposeUIResources() {
        ui.value.let { Disposer.dispose(it) }
        ui.drop()
    }

    companion object {
        fun showSettingsDialog(project: Project? = null) {
            ShowSettingsUtil.getInstance()
                .showSettingsDialog(project?.takeUnless { it.isDisposed }, SettingsConfigurable::class.java)
        }
    }
}
