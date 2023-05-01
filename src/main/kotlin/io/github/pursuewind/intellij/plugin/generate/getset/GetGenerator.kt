package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.psi.PsiMethod
import io.github.pursuewind.intellij.plugin.generate.util.firstCharToLowerCase

abstract class AbsGetCodeGenerator : AbsGetSetCodeGenerator() {
    override var methodPrefix: () -> List<String> = { listOf(GET_METHOD_PREFIX, IS_METHOD_PREFIX) }
    override var method2Code: (PsiMethod) -> String = g@{ method ->
        val expressionName = postfixData.psiElementName
        val methodName = method.name
        val prefix = if (checkGetMethodPrefix(method)) "is" else "get"
        val varName = methodName.replaceFirst(prefix, "").firstCharToLowerCase()
        val psiType = method.returnType ?: return@g "$expressionName.$methodName();"

        val typeName = psiType.canonicalText

        "$typeName $varName = $expressionName.$methodName();"
    }
}

object GetCodeGenerator : AbsGetCodeGenerator()

object GetNoSuperCodeGenerator : AbsGetCodeGenerator() {
    override var generateSuperClass: () -> Boolean = { false }
}