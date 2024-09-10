package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.impl.JSReferenceSetElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import kotlin.properties.Delegates

class PostfixData() {
    lateinit var editor: Editor
    var psiElement: PsiElement? by Delegates.observable(null) { _, _, new ->
        new?.let {
            getPsiElementName(it) { argsPsiType ->
                val methodArgsPsiElementPair = getPsiElementName(argsPsiType)
                methodArgs0PsiElementName = methodArgsPsiElementPair.first
                methodArgs0PsiType = methodArgsPsiElementPair.second
            }
        }?.let {
            psiElementName = it.first
            psiType = it.second
        }
    }
    var psiType: PsiType? = null
    var psiElementName: String = ""

    //
    var methodArgs0PsiType: PsiType? = null
    var methodArgs0PsiElementName: String = ""

    private fun getPsiElementName(
        psiElement: PsiElement,
        consumer: ((PsiElement) -> Unit)? = null
    ): Pair<String, PsiType?> {
        when (psiElement) {
            is PsiMethodCallExpression -> {

                val expressions = psiElement.argumentList.expressions

                expressions.firstOrNull()?.let {
                    if (it is PsiMethodCallExpression) {
                        consumer?.invoke(it)
                    }
                }

                val psiReferenceExpression = psiElement.methodExpression.qualifierExpression as? PsiReferenceExpression
                return (psiReferenceExpression?.text ?: "") to (psiReferenceExpression?.type ?: PsiType.getTypeByName(
                    psiReferenceExpression?.canonicalText!!,
                    psiElement.project,
                    GlobalSearchScope.allScope(psiElement.project)
                ))
            }

            is PsiReferenceExpression -> {
                return psiElement.canonicalText to (psiElement.type ?: PsiType.getTypeByName(
                    psiElement.canonicalText,
                    psiElement.project,
                    GlobalSearchScope.allScope(psiElement.project)
                ))
            }

            is PsiExpression -> {
                if (psiElement.type == null) {
                    throw IllegalStateException("")
                }
                return psiElement.text to psiElement.type
            }

            is PsiJavaCodeReferenceElement -> {
                return psiElement.canonicalText to PsiType.getTypeByName(
                    psiElement.canonicalText,
                    psiElement.project,
                    GlobalSearchScope.allScope(psiElement.project)
                )
            }

            is PsiIdentifier -> {
                return psiElement.text to PsiType.getTypeByName(
                    psiElement.text,
                    psiElement.project,
                    GlobalSearchScope.allScope(psiElement.project)
                )
            }

            is JSReferenceExpression -> {
                return psiElement.text to PsiType.getTypeByName(
                    psiElement.text,
                    psiElement.project,
                    GlobalSearchScope.allScope(psiElement.project)
                )
            }

            else -> throw Exception("type not supported: " + psiElement)
        }
    }
}