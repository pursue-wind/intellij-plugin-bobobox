package io.github.pursuewind.intellij.plugin.generate;

import io.github.pursuewind.intellij.plugin.generate.getset.*
import io.github.pursuewind.intellij.plugin.generate.trans.*


enum class PostfixEnum(
    val postfix: String,
    val example: String,
) {

    // get set
    GET("_get", message("postfix.get.example")),
    GET_NO_SUPER("_gets", message("postfix.get_no_super.example")),
    SET("_set", message("postfix.set.example")),
    SET_NO_SUPER("_sets", message("postfix.set_no_super.example")),
    SET_NO_DEFAULT("_setn", message("postfix.set_no_default.example")),
    SET_LOMBOK_CHAIN("_setc", message("postfix.set_lombok_chain.example")),
    BUILDER("_build", message("postfix.build.example")),
    BUILDER_NO_DEFAULT("_buildn", message("postfix.build_no_default.example")),
    GET_SET("_gset", message("postfix.get_set.example")),

    // convert
    CC("_cc", message("postfix.convert.under_score_to_camel_case.example")),
    UL("_ul", message("postfix.convert.camel_case_to_under_score.example")),
    ;


    val generator: CodeGenerator
        get() {
            return when (this) {
                GET -> GetCodeGenerator
                GET_NO_SUPER -> GetNoSuperCodeGenerator
                SET -> SetCodeGenerator
                SET_NO_SUPER -> SetNoSuperCodeGenerator
                SET_NO_DEFAULT -> SetNoDefaultCodeGenerator
                SET_LOMBOK_CHAIN -> SetLombokChainCodeGenerator
                BUILDER -> BuilderCodeGenerator()
                BUILDER_NO_DEFAULT -> BuilderCodeNoDefaultValGenerator
                GET_SET -> GetSetCodeGenerator

                CC -> CcCodeGenerator
                UL -> UlCodeGenerator
            }
        }

    companion object {
        fun getCodeGenerator(postfix: String): CodeGenerator {
            return values().find { it.postfix == postfix }?.generator
                ?: throw IllegalArgumentException("Unknown code generator")
        }
    }
}