package io.github.pursuewind.intellij.plugin.ui

import com.intellij.ui.components.JBScrollPane
import io.github.pursuewind.intellij.plugin.inline.data.SettingsState
import io.github.pursuewind.intellij.plugin.inline.ui.settings.SettingsComponentProvider

// vm options
/**
--add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED
--add-opens=java.desktop/sun.awt.image=ALL-UNNAMED
--add-opens=java.desktop/sun.java2d=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.desktop/javax.swing=ALL-UNNAMED
--add-opens=java.desktop/sun.swing=ALL-UNNAMED
--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED
--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED
--add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED
--add-opens=java.desktop/java.awt.event=ALL-UNNAMED
--add-opens=java.desktop/java.awt=ALL-UNNAMED
--add-opens=java.desktop/sun.awt=ALL-UNNAMED
--add-opens=java.awt/java.desktop=ALL-UNNAMED
--add-opens=java.base/java.lang=ALL-UNNAMED
 */


fun main() = uiTest("Settings UI Test", 650, 900/*, true*/) {
    val panel = SettingsComponentProvider.Main(
        SettingsState()
    ).createComponent()
    JBScrollPane(panel)
}
