package io.github.pursuewind.intellij.plugin.inline.domain

import io.github.pursuewind.intellij.plugin.inline.domain.wrapper.RangeHighlighterWrapper

interface HighlightersValidator {

    fun isValid(highlighter: RangeHighlighterWrapper): Boolean
}