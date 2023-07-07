package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.impl.source.PsiClassReferenceType
import io.github.pursuewind.intellij.plugin.generate.util.firstCharToLowerCase
import java.util.*


abstract class AbsSetGenerator : AbsGetSetCodeGenerator() {
    protected lateinit var _method: PsiMethod

    protected open fun generateSetMethodStr(): String = "${postfixData.psiElementName}.${_method.name}"
    protected open var assemble: (String, String) -> String = { method, args -> "$method($args);" }


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
            JavaTypeMapping.baseTypeMapping[typeNameLower] ?: when (psiType) {
                is PsiClassReferenceType -> psiClassReferenceType2Code(psiType)
                is PsiArrayType -> {
                    val componentType = psiType.componentType
                    val innerCode = (componentType as? PsiClassReferenceType)?.let { psiClassReferenceType2Code(it) }
                    "new ${psiType.canonicalText}{$innerCode}"
                }

                else -> ""
            }
        } ?: ""
    }
    protected open var psiClassReferenceType2Code: (PsiClassReferenceType) -> String = {
        it.resolve()?.let { psiCls ->
            val qualifiedName = psiCls.qualifiedName
            when {
                psiCls.isEnum -> {
                    val firstEnumName = psiCls.allFields.filterIsInstance<PsiEnumConstant>().firstOrNull()?.name
                    "${it.canonicalText}.${firstEnumName}"
                }

                else -> {
                    val extraStr = if (psiCls.isInterface) "{\n //TODO \n}" else ""
                    JavaTypeMapping.mapping[qualifiedName] ?: "new ${it.canonicalText}()$extraStr"
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
    override fun generateSetMethodStr(): String = "\n.${_method.name.replace(SET_METHOD_PREFIX, "").firstCharToLowerCase()}"
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
            val prefix = if (checkGetMethodPrefix(_method)) IS_METHOD_PREFIX else GET_METHOD_PREFIX
            val buildGetMethodName = _method.name.replaceFirst(SET_METHOD_PREFIX, prefix)

            setMethodMap[buildGetMethodName]?.let { gm -> "$argsTypeName.${gm.name}()" }
        } ?: ""
    }
}