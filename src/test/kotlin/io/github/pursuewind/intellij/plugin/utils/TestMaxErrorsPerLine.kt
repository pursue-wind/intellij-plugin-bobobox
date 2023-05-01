package io.github.pursuewind.intellij.plugin.utils

import io.github.pursuewind.intellij.plugin.inline.domain.MaxErrorsPerLine

object TestMaxErrorsPerLine : MaxErrorsPerLine {
    const val MAX_PER_LINE = 3

    override val maxPerLine: Int = MAX_PER_LINE
}