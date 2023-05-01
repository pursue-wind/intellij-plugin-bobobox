package io.github.pursuewind.intellij.plugin.generate.trans

import com.intellij.psi.PsiIdentifier
import io.github.pursuewind.intellij.plugin.generate.PostfixEnum
import io.github.pursuewind.intellij.plugin.generate.getset.AbsVariableGenerateTemplate
import io.github.pursuewind.intellij.plugin.generate.getset.defaultGetExprFunc

abstract class AbsConvertTemplate(enum: PostfixEnum) : AbsVariableGenerateTemplate(
    enum,
    { true },
    {
        when (it) {
            is PsiIdentifier -> it
            else -> defaultGetExprFunc(it)
        }
    }
)

class ToCamelCaseTemplate : AbsConvertTemplate(PostfixEnum.CC)

class ToUnderscoreTemplate : AbsConvertTemplate(PostfixEnum.UL)
