package io.github.pursuewind.intellij.plugin.inline.ui.settings

import com.intellij.util.ui.FormBuilder


interface Component {
    fun addToBuilder(formBuilder: FormBuilder)
}
