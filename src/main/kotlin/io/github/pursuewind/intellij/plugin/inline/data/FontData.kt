package io.github.pursuewind.intellij.plugin.inline.data

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.UIUtilities
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeEvent
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeListener
import io.github.pursuewind.intellij.plugin.inline.domain.settings.SettingsChangeObservable
import java.awt.Font
import java.awt.FontMetrics
import java.awt.GraphicsEnvironment

interface FontData {

    val font: Font
    val fontMetrics: FontMetrics
    val lineHeight: Int

    class BySettings(
        private val editor: Editor,
        graphicsEnvironment: GraphicsEnvironment,

        parentDisposable: Disposable,
    ) : FontData, Disposable, SettingsChangeListener {

        private val allFonts: Array<Font> = graphicsEnvironment.allFonts

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
            Disposer.register(parentDisposable, this)
        }

        private var underivedFont: Font = calculateUnderivedFont(
            ApplicationManager.getApplication().getService(MySettingsService::class.java).state.font
        )

        override val font: Font
            get() = underivedFont.deriveFont(editor.colorsScheme.editorFontSize.toFloat() - 1)


        private fun calculateUnderivedFont(settingsFontName: String): Font {

            for (font in allFonts)
                if (font.fontName == settingsFontName)
                    return font

            for (font in allFonts)
                if (font.fontName == editor.colorsScheme.editorFontName)
                    return font

            return allFonts[0]
        }

        override val fontMetrics: FontMetrics
            get() = UIUtilities.getFontMetrics(editor.component, font)

        override val lineHeight: Int
            get() = editor.lineHeight

        override fun onSettingsChange(event: SettingsChangeEvent) {
            val settingsFontName = event.newSettingsState.font

            underivedFont = calculateUnderivedFont(settingsFontName)
        }

        override fun dispose() {
            MySettingsService.OBSERVABLE.unsubscribe(this)
        }
    }

    class ByEditor(
        private val editor: Editor
    ) : FontData {
        override val font: Font
            get() = editor.colorsScheme.getFont(EditorFontType.PLAIN)
        override val fontMetrics: FontMetrics
            get() = UIUtilities.getFontMetrics(editor.component, font)
        override val lineHeight: Int
            get() = editor.lineHeight

    }
}