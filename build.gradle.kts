import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.tasks.RunPluginVerifierTask
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun properties(key: String) = project.findProperty(key).toString()
fun properties2(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)
fun dateValue(pattern: String): String = LocalDate.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern(pattern))
val pluginVersion: Provider<String> = properties2("pluginMajorVersion")

plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    id("org.jetbrains.changelog") version "2.2.0"
    kotlin("jvm") version "1.9.20"

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

repositories {
    mavenLocal()
    maven(url = "https://maven.aliyun.com/repository/public")
    maven(url = "https://maven-central.storage-download.googleapis.com/repos/central/data/")
    maven(url = "https://www.jetbrains.com/intellij-repository/releases")
    maven(url = "https://jitpack.io")
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.8.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("com.google.code.gson:gson:2.10.1")
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    header = provider { "${version.get()} (${dateValue("yyyy/MM/dd")})" }
    groups.empty()
    repositoryUrl = properties("pluginRepositoryUrl")
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
        pluginDescription = projectDir.resolve("DESCRIPTION.md").readText()
                .let { org.jetbrains.changelog.markdownToHTML(it) }

        // local variable for configuration cache compatibility
        val changelog = project.changelog
        // Get the latest available change notes from the changelog file
        changeNotes = pluginVersion.map { pluginVersion ->
            with(changelog) {
                renderItem(
                        (getOrNull(pluginVersion) ?: getUnreleased())
                                .withHeader(false)
                                .withEmptySections(false),
                        Changelog.OutputType.HTML
                )
            }
        }
    }

    // Validate plugin starting from version 2022.3.3 to save disk space
    listProductsReleases {
        sinceVersion = "2022.3.3"
    }
}