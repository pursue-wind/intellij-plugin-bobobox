package io.github.pursuewind.intellij.plugin.generate.increment

class UniversalNumberSeparator(private val parent: UniversalNumber) {
    private val separators: List<Char> = getSeparators()

    private fun getSeparators(): List<Char> {
        var digits = 0
        val separators = mutableListOf<Char>()
        for (aChar in parent.chars) {
            if (digits > 0 && !aChar.isDigit()) {
                separators.add(aChar)
            } else if (aChar.isDigit()) {
                digits++
            }
        }
        return separators
    }

    fun guessSeparator(): Char? {
        return when {
            ' ' in separators -> ' '
            separators.size > 1 && separators.first() == '.' && separators.last() == ',' -> '.'
            else -> null
        }
    }

    fun needSeparator(): Boolean {
        val separator = guessSeparator()
        return getConsecutiveDigits(separator, 0) == 3
    }

    fun getConsecutiveDigits(separator: Char?, from: Int): Int {
        if (separator == null) return -1
        var consecutiveDigits = 0
        for (i in from until parent.chars.size) {
            val aChar = parent.chars[i]
            if (aChar.isDigit()) {
                consecutiveDigits++
            } else {
                return if (separator != aChar && !(separator == '.' && aChar == ',')) {
                    -1
                } else {
                    break
                }
            }
        }
        return consecutiveDigits
    }

    fun canRemoveSeparator(from: Int): Boolean {
        //TODO
        val aChar = parent.chars[from]
        val guessed = guessSeparator()
        return isSpace(from) || aChar == '.'
                && guessed != null
                && guessed == '.'
                && getConsecutiveDigits(aChar, from + 1) == 3
    }

    private fun isSpace(i: Int): Boolean {
        return parent.chars.getOrNull(i) == ' '
    }

}
