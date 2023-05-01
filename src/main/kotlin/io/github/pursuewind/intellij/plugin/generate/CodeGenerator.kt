package io.github.pursuewind.intellij.plugin.generate

import io.github.pursuewind.intellij.plugin.generate.getset.PostfixData

interface CodeGenerator {
    fun generate(postfixData: PostfixData): String =""
}