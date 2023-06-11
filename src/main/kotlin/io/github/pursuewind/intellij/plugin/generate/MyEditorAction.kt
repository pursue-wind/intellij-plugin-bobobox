package io.github.pursuewind.intellij.plugin.generate

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorAction
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.impl.EditorComponentImpl
import com.intellij.openapi.editor.textarea.TextComponentEditorImpl
import com.intellij.ui.SpeedSearchBase
import com.intellij.ui.speedSearch.SpeedSearchSupply
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.text.JTextComponent


/**
 *
 */
abstract class MyEditorAction(defaultHandler: EditorActionHandler?) : EditorAction(defaultHandler) {

    ///from TextComponentEditorAction
    override fun getEditor(dataContext: DataContext): Editor? = getEditorFromContext(dataContext)

    override fun getActionUpdateThread() = ActionUpdateThread.EDT

    companion object {
        private fun getEditorFromContext(dataContext: DataContext): Editor? {
            val editor = CommonDataKeys.EDITOR.getData(dataContext)
            if (editor != null) return editor
            val project = CommonDataKeys.PROJECT.getData(dataContext)
            val data: Any? = PlatformCoreDataKeys.CONTEXT_COMPONENT.getData(dataContext)
            if (data is EditorComponentImpl) {
                // can happen if editor is already disposed, or if it's in renderer mode
                return null
            }
            if (data is JTextComponent) {
                return TextComponentEditorImpl(project, (data as JTextComponent?)!!)
            }
            if (data is JComponent) {
                val field = findActiveSpeedSearchTextField(data)
                if (field != null) {
                    return TextComponentEditorImpl(project, field)
                }
            }
            return null
        }

        private fun findActiveSpeedSearchTextField(c: JComponent): JTextField? {
            val supply = SpeedSearchSupply.getSupply(c)
            if (supply is SpeedSearchBase<*>) {
                return supply.searchField
            }
            if (c is DataProvider) {
                val component = PlatformDataKeys.SPEED_SEARCH_COMPONENT.getData((c as DataProvider))
                if (component is JTextField) {
                    return component
                }
            }
            return null
        }
    }
}
