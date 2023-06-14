import org.jetbrains.intellij.tasks.RunPluginVerifierTask

fun properties(key: String) = project.findProperty(key).toString()
fun properties2(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    id("org.jetbrains.changelog") version "2.0.0"
    kotlin("jvm") version "1.7.10"

    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
    // Gradle Kover Plugin
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties2("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

java {
    toolchain {
        version = 17
    }
}

kotlin {
    jvmToolchain {
        version = 17
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.8.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("com.google.code.gson:gson:2.10.1")
}
tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    runPluginVerifier {
        failureLevel.set(RunPluginVerifierTask.FailureLevel.ALL)
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        val myReadMe = file("README.md").readText()

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                myReadMe.lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n")
                        .replace("example.png", "mini_example.png")
                        .let { org.jetbrains.changelog.markdownToHTML(it) }
        )

        changeNotes.set(
                myReadMe.lines().run {
                    val start = "<!-- Change notes -->"
                    val end = "<!-- Change notes end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Change notes section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n") { it.replace("* ", "\n") }
                        .let { org.jetbrains.changelog.markdownToHTML(it) }
        )
    }
}