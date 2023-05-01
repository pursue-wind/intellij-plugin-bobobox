package io.github.pursuewind.intellij.plugin.generate

import io.github.pursuewind.intellij.plugin.generate.getset.PostfixData



abstract class AbsCodeGenerator() : CodeGenerator {
    lateinit var postfixData: PostfixData
    override fun generate(postfixData: PostfixData): String = checkError {
        this.postfixData = postfixData
        doGenerator()
    }

    protected abstract fun doGenerator(): String

    private inline fun <T> checkError(action: () -> T): T = try {
        action()
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        onError(throwable)
    }

    protected open fun createErrorInfo(throwable: Throwable): ErrorInfo? {
//        val errorMessage = when (throwable) {
//            is UnsupportedLanguageException -> message("error.unsupportedLanguage", throwable.lang.langName)
//            is ContentLengthLimitException -> message("error.text.too.long")
//            is HttpRequests.HttpStatusException -> when (throwable.statusCode) {
//                HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE.code() -> message("error.text.too.long")
//                else -> throwable.getCommonMessage()
//            }
//
//            is IOException -> throwable.getCommonMessage()
//            else -> return null
//        }

        throw Exception()
    }

    protected fun onError(throwable: Throwable): Nothing {
        val errorInfo = createErrorInfo(throwable) ?: throw throwable
        throw Exception()
    }

    protected open fun beforeAppend(): String {
        return ""
    }

    protected open fun afterAppend(): String {
        return ""
    }


}