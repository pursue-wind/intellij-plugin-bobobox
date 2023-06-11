package io.github.pursuewind.intellij.plugin.generate.getset

import com.intellij.psi.*
import com.intellij.psi.JavaTokenType.*
import io.github.pursuewind.intellij.plugin.generate.PostfixEnum
import org.apache.commons.lang.StringUtils

/**
 * @author pursue_wind
 * @date 2021/10/1
 */

class GenerateGetterNoSuper : AbsVariableGenerateTemplate(PostfixEnum.GET_NO_SUPER)
class GenerateGetter : AbsVariableGenerateTemplate(PostfixEnum.GET)

class GenerateSetter : AbsVariableGenerateTemplate(PostfixEnum.SET)
class GenerateSetterLombokChain : AbsVariableGenerateTemplate(PostfixEnum.SET_LOMBOK_CHAIN)
class GenerateSetterNoDefault : AbsVariableGenerateTemplate(PostfixEnum.SET_NO_DEFAULT)
class GenerateSetterNoSuper : AbsVariableGenerateTemplate(PostfixEnum.SET_NO_SUPER)

class GenerateBuilder : AbsVariableGenerateTemplate(PostfixEnum.BUILDER, { check(it) }) {
    companion object {
        private fun check(psiElement: PsiElement): Boolean {
            if (psiElement is PsiJavaToken) {
                // PsiReferenceExpression:  Xxx.builder
                return when (psiElement.tokenType) {
                    RPARENTH -> psiElement.getParent()?.prevSibling?.lastChild?.text == "builder"
                    SEMICOLON -> psiElement.getPrevSibling()?.firstChild?.lastChild?.text == "builder"
//                    IDENTIFIER -> psiElement.text != null && psiElement.parent is PsiReferenceExpression
                    else -> false
                }
            } else if (psiElement is PsiReferenceExpression) {
                return StringUtils.isNotBlank(psiElement.canonicalText)
            }
            return false
        }
    }
}

class GenerateGetSet : AbsVariableGenerateTemplate(PostfixEnum.GET_SET, { check(it) }) {
    companion object {
        private fun check(psiElement: PsiElement): Boolean {
            when (psiElement) {
                is PsiIdentifier -> {
                    val parent = psiElement.getParent()
                    if (parent is PsiReferenceExpression) {
                        if (parent.type != null) {
                            return true
                        }
                    }
                }

                is PsiExpression -> {
                    val psiType = psiElement.type
                    return psiType != null
                }

                is PsiJavaToken -> {
                    val curPsiElement: PsiElement? = when (psiElement.tokenType) {
                        // 分号 ;
                        SEMICOLON -> psiElement.getPrevSibling()
                        // 右括号 )
                        RPARENTH -> psiElement.getParent()?.parent
                        else -> null
                    }
                    if (curPsiElement is PsiMethodCallExpression) {
                        return curPsiElement.methodExpression.qualifierExpression?.type != null
                                && curPsiElement.argumentList.expressions.firstOrNull() is PsiMethodCallExpression
                    }
                }
            }
            return false
        }
    }
}