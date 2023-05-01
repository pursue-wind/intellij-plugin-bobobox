package io.github.pursuewind.intellij.plugin.generate.log

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.macro.SuggestVariableNameMacro
import com.intellij.codeInsight.template.postfix.templates.editable.JavaEditablePostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.editable.JavaPostfixTemplateExpressionCondition
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.PsiElement
import io.github.pursuewind.intellij.plugin.generate.GeneratePostfixTemplateProvider

abstract class BaseLogPostfixTemplate(logLevel: String) : JavaEditablePostfixTemplate(
    "_log${if (logLevel == "info") "" else logLevel[0]}",
    "log.$logLevel(\"\$DETAIL$ \$EXPR$ = [{}]\", \$EXPR$);\$END$",
    "log.$logLevel(expr = expr)",
    setOf(JavaPostfixTemplateExpressionCondition.JavaPostfixTemplateNonVoidExpressionCondition()),
    LanguageLevel.JDK_1_6,
    true,
    GeneratePostfixTemplateProvider()
) {

    override fun addTemplateVariables(element: PsiElement, template: Template) {
        val name = MacroCallNode(SuggestVariableNameMacro())
        template.addVariable("DETAIL", name, name, true)
    }
}


class LogInfoPostfixTemplate : BaseLogPostfixTemplate("info")
class LogErrorPostfixTemplate : BaseLogPostfixTemplate("error")
class LogWarnPostfixTemplate : BaseLogPostfixTemplate("warn")
class LogDebugPostfixTemplate : BaseLogPostfixTemplate("debug")
class LogTracePostfixTemplate : BaseLogPostfixTemplate("trace")
