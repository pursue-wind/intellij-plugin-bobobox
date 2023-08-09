package io.github.pursuewind.intellij.plugin.generate.increment

import io.github.pursuewind.intellij.plugin.generate.increment.A.Companion.findLast
import io.github.pursuewind.intellij.plugin.generate.increment.A.Companion.findNext
import io.github.pursuewind.intellij.plugin.generate.increment.A.Companion.strEnumMap
import org.apache.commons.lang3.ArrayUtils


enum class A(val value: Array<String>) {
    NUM_CN(
        arrayOf(
            "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九",
            "三十", "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九",
            "四十", "四十一", "四十二", "四十三", "四十四", "四十五", "四十六", "四十七", "四十八", "四十九",
            "五十", "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九",
            "六十", "六十一", "六十二", "六十三", "六十四", "六十五", "六十六", "六十七", "六十八", "六十九",
            "七十", "七十一", "七十二", "七十三", "七十四", "七十五", "七十六", "七十七", "七十八", "七十九",
            "八十", "八十一", "八十二", "八十三", "八十四", "八十五", "八十六", "八十七", "八十八", "八十九",
            "九十", "九十一", "九十二", "九十三", "九十四", "九十五", "九十六", "九十七", "九十八", "九十九"
        )
    ),
    A_Z(('a'..'z').union(('A'..'Z')).map { it.toString() }.toTypedArray()),
    WEEK(arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")),
    WEEK2(arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")),
    WEEK3(arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")),
    WEEK4(arrayOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")),

    MONTH(
        arrayOf(
            "一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "十二月"
        )
    ),
    MONTH_EN(
        arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    ),
    MONTH_EN_SIMPLE(
        arrayOf(
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
        )
    ),

    SEXAGENARY(arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")),

    COLORS(arrayOf("红", "橙", "黄", "绿", "青", "蓝", "紫")),
    COLORS_EN(arrayOf("RED", "ORANGE", "YELLOW", "GREEN", "CYAN", "BLUE", "PURPLE"))

    ;

    companion object {
        val strEnumMap: Map<String, A> =
            A.values().flatMap { it.value.let { arr -> arr.map { strIt -> strIt to it } } }.toMap()

        fun findNext(str: String): String {
            val e = strEnumMap[str] ?: throw IllegalArgumentException("parameter error")
            val size = e.value.size
            return e.value.indexOf(str).let {
                if (it + 1 < size) e.value[it + 1]
                else e.value[0]
            }
        }

        fun findLast(str: String): String {
            val e = strEnumMap[str] ?: throw IllegalArgumentException("parameter error")
            return e.value.indexOf(str).let {
                if (it - 1 >= 0) e.value[it - 1]
                else e.value[e.value.size - 1]
            }
        }
    }

}

class UniversalNumber(textPart: String) {
    var chars: CharArray = textPart.toCharArray()
    private val separator: UniversalNumberSeparator = UniversalNumberSeparator(this)

    fun increment(): String {
        if (isNegative) {
            shiftDown(chars.size - 1)
        } else {
            shiftUp(chars.size - 1)
        }
        if (isZero && isNegative) {
            chars = ArrayUtils.remove(chars, 0)
        }
        return String(chars)
    }

    private fun decrement(): String {
        if (isZero || isNegative) {
            shiftUp(chars.size - 1)
            if (!isNegative) {
                if (chars[0] == '+') {
                    chars[0] = '-'
                } else {
                    chars = ArrayUtils.add(chars, 0, '-')
                }
            }
        } else {
            shiftDown(chars.size - 1)
        }
        return String(chars)
    }

    private fun shiftUp(start: Int) {
        if (start == -1) {
            addExtraPlace(0)
            return
        }
        for (i in start downTo 0) {
            val aChar = chars[i]
            if (Character.isDigit(aChar)) {
                val num = Character.getNumericValue(aChar)
                chars[i] = Character.forDigit((num + 1) % 10, 10)
                if (num == 9) {
                    shiftUp(i - 1)
                    break
                }
                break
            } else if (i == 0 && aChar == '-') {
                addExtraPlace(1)
                break
            }
        }
    }

    //todo what is the proper name?
    private fun addExtraPlace(index: Int) {
        if (separator.needSeparator()) {
            val character = separator.guessSeparator()
            character?.let { chars = ArrayUtils.add(chars, index, it) }
        }
        chars = ArrayUtils.add(chars, index, '1')
    }

    private fun shiftDown(start: Int) {
        for (i in start downTo 0) {
            val aChar = chars[i]
            if (Character.isDigit(aChar)) {
                val num = Character.getNumericValue(aChar)
                if (canShiftDownNext(i)) {
                    chars[i] = Character.forDigit(if (num == 0) 9 else num - 1, 10)
                    if (num == 0) {
                        shiftDown(i - 1)
                        break
                    }
                } else {
                    if (num - 1 == 0 && canRemove(i)) {
                        chars = ArrayUtils.remove(chars, i)
                        if (separator.canRemoveSeparator(i)) {
                            chars = ArrayUtils.remove(chars, i)
                        }
                    } else {
                        chars[i] = Character.forDigit(num - 1, 10)
                    }
                }
                break
            }
        }
    }

    private fun canRemove(i: Int): Boolean {
        for (j in i + 1 until chars.size) {
            val aChar = chars[j]
            return if (Character.isDigit(aChar)) {
                true
            } else if (separator.canRemoveSeparator(j)) {
                continue
            } else {
                false
            }
        }
        return false
    }


    private fun canShiftDownNext(i: Int): Boolean {
        for (j in i - 1 downTo 0) {
            val aChar = chars[j]
            if (Character.isDigit(aChar)) {
                return true
            }
        }
        return false
    }

    private val isNegative: Boolean
        get() = chars[0] == '-'

    private val isZero: Boolean
        get() = chars.all { !Character.isDigit(it) || it == '0' }

    companion object {
        const val UNIVERSAL_NUMBER_REGEX = "[+-]?\\d+([\\., ]\\d+)*"

        fun increment(s: String): String {
            val mapVal = strEnumMap[s]
            if (mapVal != null) {
                return findNext(s)
            }
            return UniversalNumber(s).increment()
        }

        fun decrement(s: String): String {
            val mapVal = strEnumMap[s]
            if (mapVal != null) {
                return findLast(s)
            }
            return UniversalNumber(s).decrement()
        }
    }
}
