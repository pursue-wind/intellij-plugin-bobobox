package io.github.pursuewind.intellij.plugin.generate.trans

import com.intellij.openapi.application.ApplicationManager
import io.github.pursuewind.intellij.plugin.generate.util.Http
import io.github.pursuewind.intellij.plugin.generate.AbsCodeGenerator
import java.util.concurrent.Callable
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


data class YuoDaoTranslation(
    val src: String,
    val tgt: String
)

data class YuoDaoTranslationResponse(
    val type: String,
    val errorCode: Int,
    val elapsedTime: Int,
    val translateResult: List<List<YuoDaoTranslation>>
)


fun youDaoRequest(chinese: String): String {
    var future = ApplicationManager.getApplication().executeOnPooledThread(
        Callable<String> {
            val url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=${chinese}"
            val resp = Http.get<YuoDaoTranslationResponse>(url)
            if (resp.errorCode == 0) resp.translateResult.first().first().tgt else ""
        }
    )

    return future.get(700, TimeUnit.MILLISECONDS)
}
