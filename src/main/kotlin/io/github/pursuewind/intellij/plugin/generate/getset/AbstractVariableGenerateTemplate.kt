package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelectorBase
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.ecma6.TypeScriptFunction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.containers.ContainerUtil
import io.github.pursuewind.intellij.plugin.generate.PostfixEnum


val defaultHasExprFunc: (PsiElement) -> Boolean = { psiElement ->
    when (psiElement) {
        is PsiIdentifier -> {
            val parent = psiElement.parent
            parent is PsiReferenceExpression && parent.type != null
        }

        is PsiExpression -> psiElement.type != null
        else -> false
    }
}

val defaultGetExprFunc: (PsiElement) -> PsiElement? = { JavaPostfixTemplatesUtils.getTopmostExpression(it) }

abstract class AbsVariableGenerateTemplate(
    private val postfixEnum: PostfixEnum,
    hasExprFunc: (PsiElement) -> Boolean = defaultHasExprFunc,
    getExprFunc: (PsiElement) -> PsiElement? = defaultGetExprFunc,
) : AbsPostfixTemplate(
    postfixEnum.postfix,
    postfixEnum.example,
    hasExprFunc,
    getExprFunc
) {

    override fun doExpandForChooseExpression(postfixData: PostfixData) {
        val codeGenerator = PostfixEnum.getCodeGenerator(postfixEnum.postfix)

        val code = codeGenerator.generate(postfixData)
        val expression = postfixData.psiElement ?: return
        val editor = postfixData.editor
        val manager = TemplateManager.getInstance(expression.project)
        val template = manager.createTemplate("", "", "$code\$END$")
        template.isToReformat = true

        removeExpressionFromEditor(expression, editor)

        manager.startTemplate(editor, template)
    }

}

abstract class AbsPostfixTemplate(
    templateName: String,
    example: String,
    hasExprFunc: (PsiElement) -> Boolean,
    // 获取传到 expandForChooseExpression 方法的 PsiElement 数据, 与 isApplicable 保持一致即可
    getExprFunc: (PsiElement) -> PsiElement?
) : PostfixTemplateWithExpressionSelector(
    templateName, templateName, example,
    object : PostfixTemplateExpressionSelectorBase({ true }) {
        override fun getNonFilteredExpressions(
            context: PsiElement,
            document: Document,
            offset: Int
        ): MutableList<PsiElement> =
            ContainerUtil.createMaybeSingletonList(getExprFunc(context) ?: context.context)

        override fun getFilters(offset: Int): Condition<PsiElement> = Condition { true }
        override fun hasExpression(context: PsiElement, copyDocument: Document, newOffset: Int) =
            hasExprFunc.invoke(context)
    },
    null
) {
    override fun expandForChooseExpression(expression: PsiElement, editor: Editor) = try {
        val postfixData = PostfixData()
        postfixData.editor = editor
        postfixData.psiElement = expression
        doExpandForChooseExpression(postfixData)
    } catch (e: Throwable) {
        e.printStackTrace()
    } finally {
        destroy(expression, editor)
    }

    protected abstract fun doExpandForChooseExpression(postfixData: PostfixData)

    protected fun removeExpressionFromEditor(expression: PsiElement, editor: Editor) {
        if (expression is PsiJavaCodeReferenceElement
            || expression is PsiIdentifier
            || expression is JSReferenceExpression
        ) {
            editor.document.deleteString(expression.textRange.startOffset, expression.textRange.endOffset)
            return
        }
        var localExpression = expression
        while (localExpression.parent !is PsiCodeBlock) {
            localExpression = localExpression.parent
        }
        var endOffset = localExpression.textRange.endOffset
        val text = editor.document.getText(TextRange(endOffset, endOffset + 1))
        if (";" == text) {
            endOffset += 1
        }
        editor.document.deleteString(localExpression.textRange.startOffset, endOffset)
    }

    protected fun destroy(expression: PsiElement?, editor: Editor?) {}

}