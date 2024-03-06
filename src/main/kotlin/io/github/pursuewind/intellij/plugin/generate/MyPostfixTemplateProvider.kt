package io.github.pursuewind.intellij.plugin.generate

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import io.github.pursuewind.intellij.plugin.generate.log.*
import io.github.pursuewind.intellij.plugin.generate.getset.*
import io.github.pursuewind.intellij.plugin.generate.trans.*

/**
 * 实现 PostfixTemplateProvider 接口注册模板
 *
 * @author pursue_wind
 */

abstract class BasePostfixTemplateProvider : PostfixTemplateProvider {
    override fun isTerminalSymbol(currentChar: Char) = false
    override fun preExpand(file: PsiFile, editor: Editor) {}
    override fun afterExpand(file: PsiFile, editor: Editor) {}
    override fun preCheck(copyFile: PsiFile, realEditor: Editor, currentOffset: Int) = copyFile
}

class GeneratePostfixTemplateProvider : BasePostfixTemplateProvider() {
    override fun getTemplates() = mutableSetOf<PostfixTemplate>(
        // get set builder
        GenerateGetter(),
        GenerateGetterNoSuper(),
        GenerateSetter(),
        GenerateSetterLombokChain(),
        GenerateSetterNoDefault(),
        GenerateSetterNoSuper(),
        GenerateBuilder(),
        GenerateGetSet(),
        // 驼峰下划线转换
        ToCamelCaseTemplate(),
        ToUnderscoreTemplate(),
    )
}


class LogPostfixTemplateProvider : BasePostfixTemplateProvider() {
    override fun getTemplates() = setOf(
        // log
        LogInfoPostfixTemplate(),
        LogErrorPostfixTemplate(),
        LogWarnPostfixTemplate(),
        LogDebugPostfixTemplate(),
        LogTracePostfixTemplate(),
    )

    private fun checkLombokExist(project: Project?, element: PsiElement): Boolean {
        val moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(element) ?: return false
        return PsiShortNamesCache.getInstance(project)
            .getClassesByName("Slf4j", GlobalSearchScope.moduleRuntimeScope(moduleForPsiElement, false))
            .any { it.qualifiedName == "lombok.extern.slf4j.Slf4j" }
    }

    override fun afterExpand(file: PsiFile, editor: Editor) {
        // Add log statement
        val project = file.project
        if (file.fileType == JavaFileType.INSTANCE) {
            val psiElementFactory = JavaPsiFacade.getElementFactory(project)
            val psiClass = (file as PsiJavaFile).classes[0]

            val slf4jAnnotation = psiClass.getAnnotation("lombok.extern.slf4j.Slf4j")
            if (checkLombokExist(project, psiClass)) {
                if (slf4jAnnotation == null) {
                    WriteCommandAction.runWriteCommandAction(
                        project,
                        "add lombok anno",
                        null, {
                            psiClass.firstChild?.let { psiElement ->
                                // Add Slf4j import
                                file.importList?.let { its ->
                                    if (its.children.map { it.text }
                                            .any { it.contains("lombok.extern.slf4j") }) return@let
                                    val elementFactory = JavaPsiFacade.getElementFactory(project)
                                    val importStatement =
                                        elementFactory.createImportStatementOnDemand("lombok.extern.slf4j")
                                    its.add(importStatement)
                                }
                                // Add Lombok annotation
//                            val createAnnotationFromText = psiElementFactory.createAnnotationFromText("@Slf4j\n", psiClass)
                                val createAnnotationFromText =
                                    psiElementFactory.createStatementFromText("@Slf4j\n", psiClass)

                                if (psiElement is PsiDocComment) {
                                    val nextSibling = psiElement.nextSibling
                                    psiClass.addBefore(createAnnotationFromText, nextSibling)
                                } else {
                                    psiClass.addBefore(createAnnotationFromText, psiElement)
                                }
                            }
                        }, file
                    )
                }
            } else {

                val loggerName = psiClass.name ?: file.name
                val elementFactory = JavaPsiFacade.getElementFactory(project)

                val psiLoggerField = psiClass.findFieldByName("log", false) ?: psiClass.findFieldByName("logger", false)
                if (psiLoggerField == null) {
                    val createFieldFromText = psiElementFactory.createFieldFromText(
                        "private static final Logger log = LoggerFactory.getLogger($loggerName.class);",
                        psiClass
                    )
                    psiClass.firstChild?.let { psiElement ->
                        // import org.slf4j.Logger; import org.slf4j.LoggerFactory;
                        file.importList?.let { its ->
                            if (its.children.map { it.text }.any { it.contains("org.slf4j") }) return

                            val importStatement = elementFactory.createImportStatementOnDemand("org.slf4j")
                            its.add(importStatement)
                        }
                        psiClass.addAfter(createFieldFromText, psiClass.lBrace)
                    }
                }
            }
        }
    }

}