package io.github.pursuewind.intellij.plugin.generate

import io.github.pursuewind.intellij.plugin.generate.getset.PostfixData


abstract class AbsCodeGenerator : CodeGenerator {
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
        throw throwable
    }

    protected open fun beforeAppend() = ""
    protected open fun afterAppend() = ""
}