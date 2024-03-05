package io.github.pursuewind.intellij.plugin.generate.trans

import io.github.pursuewind.intellij.plugin.generate.util.Http
import io.github.pursuewind.intellij.plugin.generate.AbsCodeGenerator
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


// 下划线转驼峰 Underline to CamelCase _cc
// 驼峰转下划线 CamelCase to Underline _ul

// 翻译并转驼峰 Translate to CamelCase _tcc
// 翻译并转下划线 Translate to Underline _tul
val toCamelCase: (String) -> String = {
    val builder = StringBuilder()
    var upperCase = false
    for (i in it.indices) {
        val c = it[i]
        upperCase = if (c == ' ' || c == '-' || c == '_') {
            true
        } else {
            builder.append(if (upperCase) c.uppercaseChar() else c)
            false
        }
    }
    builder.toString()
}


val toUnderscore: (String) -> String = { s ->
    s.replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
        .replace(Regex("[ -]"), "_")
        .lowercase()
}

val doNothingStr: (String) -> String = { it }

fun chineseCheck(text: String, func: (String) -> String): String {
    // 判断文本语言类型
    val isChinese = text.contains("[\u4e00-\u9fa5]".toRegex())

    // 将中文翻译为英文
    val translatedText = if (isChinese) {
        // 使用翻译 API 进行翻译
        youDaoRequest(text)
    } else {
        text
    }

    // 文本转换
    return func(translatedText)
}

object CcCodeGenerator : AbsCodeGenerator() {
    override fun doGenerator() = chineseCheck(postfixData.psiElementName, toCamelCase)
}

object UlCodeGenerator : AbsCodeGenerator() {
    override fun doGenerator() = chineseCheck(postfixData.psiElementName, toUnderscore)
}


data class TranslationResponse(
    val from: String,
    val to: String,
    val trans_result: List<Translation>
)

data class Translation(
    val src: String,
    val dst: String
)

var api = TransApi("20220520001222370", "xxx")

fun youDaoRequest(chinese: String): String {
    val url = api.buildUrl(chinese)
    val f = CompletableFuture.supplyAsync {
        return@supplyAsync Http.get<TranslationResponse>(url)
    }

    val resp = f.get(2, TimeUnit.SECONDS)
    return resp?.trans_result?.first()?.dst ?: ""
}
