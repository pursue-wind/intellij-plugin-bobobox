package io.github.pursuewind.intellij.plugin.utils

import io.github.pursuewind.intellij.plugin.inline.domain.wrapper.RangeHighlighterWrapper

open class TestRangeHighlighterWrapper(
    override val priority: Int,
    override val description: String,
    override val offset: Int,
    override val lineNumber: Int,
    private val isSufficient: Boolean,
    private val isValidInDocument: Boolean,
) : RangeHighlighterWrapper {

    override fun isSufficient(): Boolean = isSufficient

    override fun isValidInDocument(): Boolean = isValidInDocument
}