package io.github.pursuewind.intellij.plugin.generate.util

import java.net.URLEncoder
import java.security.MessageDigest

//import com.google.common.base.CaseFormat
///**
// * CamelCase to underscore
// */
//fun String.camelToUnderline(): String = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)
//
///**
// * underscore to  CamelCase
// */
//fun String.underlineToCamel(): String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this)


/**
 * URL编码
 */
fun String.urlEncode(): String = if (isEmpty()) this else URLEncoder.encode(this, "UTF-8")

/**
 * 字符串首字母大写转小写
 */
fun String.firstCharToLowerCase(): String {
    val cs = this.toCharArray()
    val cs0 = cs[0].code
    if (cs0 in 65..90) cs[0] = cs[0] + 32
    return String(cs)
}


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}

