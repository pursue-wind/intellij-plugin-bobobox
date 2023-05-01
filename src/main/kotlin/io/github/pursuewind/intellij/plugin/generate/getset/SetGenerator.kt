package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.impl.source.PsiClassReferenceType
import io.github.pursuewind.intellij.plugin.generate.util.firstCharToLowerCase
import java.util.*


abstract class AbsSetGenerator : AbsGetSetCodeGenerator() {
    protected lateinit var _method: PsiMethod

    protected open fun generateSetMethodStr(): String = "${postfixData.psiElementName}.${_method.name}"
    protected open var assemble: (String, String) -> String =
        { method, args -> "$method($args);" }

    override var method2Code: (PsiMethod) -> String = { method ->
        this._method = method
        val parameterList = method.parameterList
        val parameters = parameterList.parameters

        assemble(generateSetMethodStr(), generateSetMethodArgsStr(parameters))
    }

    protected open var generateSetMethodArgsStr: (Array<PsiParameter>) -> String = { parameters ->
        parameters.firstOrNull()?.let { psiParameter ->
            val psiType = psiParameter.type
            val typeName = psiType.presentableText
            // if PsiClassReferenceType, get from default map else new
            val typeNameLower = typeName.lowercase(Locale.getDefault())
            JavaTypeMapping.baseTypeMapping[typeNameLower] ?: (psiType as? PsiClassReferenceType)?.let {
                it.resolve()?.let { psiCls ->
                    val qualifiedName = psiCls.qualifiedName
                    JavaTypeMapping.mapping[qualifiedName] ?: "new ${it.canonicalText}()"
                }
            }
        } ?: ""
    }

}


object SetCodeGenerator : AbsSetGenerator()

object SetNoSuperCodeGenerator : AbsSetGenerator() {
    override var generateSuperClass: () -> Boolean = { false }
}

object SetNoDefaultCodeGenerator : AbsSetGenerator() {
    override var generateSetMethodArgsStr: (Array<PsiParameter>) -> String = { "" }
}

object SetLombokChainCodeGenerator : AbsSetGenerator() {

    override fun beforeAppend() = postfixData.psiElementName
    override fun afterAppend() = ";"

    override fun generateSetMethodStr(): String = "\n.${_method.name}"
    override var assemble: (String, String) -> String = { method, args -> "$method($args)" }
}


open class BuilderCodeGenerator : AbsSetGenerator() {
    override fun beforeAppend() = postfixData.psiElementName + ".builder()"
    override fun afterAppend() = "\n.build();"
    override var assemble: (String, String) -> String = { method, args -> "$method($args)" }
    override fun generateSetMethodStr(): String = "\n.${_method.name.replace("set", "").firstCharToLowerCase()}"
}


object BuilderCodeNoDefaultValGenerator : BuilderCodeGenerator() {
    override var generateSetMethodArgsStr: (Array<PsiParameter>) -> String = { "" }
}

object GetSetCodeGenerator : AbsSetGenerator() {
    override var generateSetMethodArgsStr: (Array<PsiParameter>) -> String = g@{ parameters ->
        val argsType = postfixData.methodArgs0PsiType ?: return@g ""
        val argsTypeName = postfixData.methodArgs0PsiElementName
        val psiCls = psiType2PsiCls(argsType)
        val argsGetMethodList =
            psiCls?.let { generateMethodListByClass(it, listOf(GET_METHOD_PREFIX, IS_METHOD_PREFIX)) } ?: return@g ""
        val setMethodMap = argsGetMethodList.associateBy { it.name }

        parameters.firstOrNull()?.let {
            val prefix = if (checkGetMethodPrefix(_method)) "is" else "get"
            val buildGetMethodName = _method.name.replaceFirst("set", prefix)

            setMethodMap[buildGetMethodName]?.let { gm -> "$argsTypeName.${gm.name}()" }
        } ?: ""
    }
}