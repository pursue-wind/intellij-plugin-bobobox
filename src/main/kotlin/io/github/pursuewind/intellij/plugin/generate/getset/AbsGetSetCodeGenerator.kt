package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import io.github.pursuewind.intellij.plugin.generate.AbsCodeGenerator


abstract class AbsGetSetCodeGenerator() : AbsCodeGenerator() {

    protected open var method2Code: (PsiMethod) -> String = { "" }
    protected open var generateSuperClass: Boolean = true
    protected open var methodPrefix: List<String> = listOf(SET_METHOD_PREFIX)

    private fun isValidMethod(method: PsiMethod, prefixArray: List<String>) =
        method.hasModifierProperty(PsiModifier.PUBLIC) &&
                !method.hasModifierProperty(PsiModifier.STATIC) &&
                (prefixArray.isEmpty() || prefixArray.any { method.name.startsWith(it) })

    protected fun generateMethodListByClass(
        psiClass: PsiClass,
        prefixList: List<String> = emptyList()
    ): List<PsiMethod> =
        (
            psiClass.methods
                .filter {
                    if (prefixList.isEmpty()) isValidMethod(it, methodPrefix)
                    else isValidMethod(it, prefixList)
                } + (psiClass.superClass?.takeIf {
                generateSuperClass && (it.qualifiedName?.startsWith("java.")?.not() ?: false)
            }?.let { generateMethodListByClass(it) } ?: emptySet())
        ).distinctBy { it.name }


    fun psiType2PsiCls(psiType: PsiType): PsiClass? {
        return when (psiType) {
            is PsiClassReferenceType -> psiType.resolve()
            is PsiClassType -> psiType.resolve()
            else -> null
        }
    }


    override fun doGenerator(): String {
        val psiType = postfixData.psiType ?: return ""
        val psiCls = psiType2PsiCls(psiType)
        val methodList = psiCls?.let { generateMethodListByClass(it) } ?: return ""


        val code = StringBuilder()
        code.append(beforeAppend())
        for (method in methodList) {
            code.append(method2Code(method))
        }
        code.append(afterAppend())
        return code.toString()
    }


    fun checkGetMethodPrefix(method: PsiMethod): Boolean {
        val parameters = method.parameterList.parameters
        parameters.firstOrNull()?.let { parameter ->
            val psiType = parameter.type
            if (psiType !is PsiClassReferenceType) {
                return "boolean" == psiType.canonicalText
            }
        }
        return false
    }

    companion object {
        const val SET_METHOD_PREFIX = "set"
        const val GET_METHOD_PREFIX = "get"
        const val IS_METHOD_PREFIX = "is"
    }
}
