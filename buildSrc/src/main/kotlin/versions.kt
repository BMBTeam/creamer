@file:Suppress("ObjectPropertyName")

import org.gradle.api.Project

internal val Project.rpp: Map<String, *> get() = rootProject.properties // rpp == root project properties

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Common

val Project.`replacementTemplates`: Map<String, String>
    get() = mapOf(
        "version" to `mod-version`,
        "name" to `mod-name`,
        "description" to `mod-description`,
        "authors" to `mod-authors`,
        "license" to `mod-license`,
        "homepage" to `mod-homepage`,
        "sources" to `mod-sources`,
        "issues" to `mod-issues`
    )

val Project.`minecraft-version`: String get() = "${rpp["minecraft-version"]}"
val Project.`maven-group`: String get() = "${rpp["maven-group"]}"
val Project.`mod-version`: String get() = "${rpp["mod-version"]}"
val Project.`mod-id`: String get() = "${rpp["mod-id"]}"
val Project.`mod-name`: String get() = "${rpp["mod-name"]}"
val Project.`mod-description`: String get() = "${rpp["mod-description"]}"
val Project.`mod-authors`: String get() = "${rpp["mod-authors"]}"
val Project.`mod-license`: String get() = "${rpp["mod-license"]}"
val Project.`mod-homepage`: String get() = "${rpp["mod-homepage"]}"
val Project.`mod-sources`: String get() = "${rpp["mod-sources"]}"
val Project.`mod-issues`: String get() = "${rpp["mod-issues"]}"

private val Project.kotlinx: String get() = "org.jetbrains.kotlinx"
val Project.`kotlinx-serialization-json`: String get() = "$kotlinx:kotlinx-serialization-json:${rpp["serialization"]}"
val Project.`kotlinx-coroutines-core`: String get() = "$kotlinx:kotlinx-coroutines-core:${rpp["coroutines"]}"
val Project.`kotlinx-coroutines-jdk8`: String get() = "$kotlinx:kotlinx-coroutines-jdk8:${rpp["coroutines"]}"

val Project.`rei`: String get() = "me.shedaniel:RoughlyEnoughItems-api:${rpp["rei"]}"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Fabric

private val fabric: String = "net.fabricmc"
val Project.`fabric-loader`: String get() = "$fabric:fabric-loader:${rpp["fabric-loader"]}"
val Project.`fabric-api`: String get() = "$fabric.fabric-api:fabric-api:${rpp["fabric-api"]}"
val Project.`fabric-language-kotlin`: String get() = "$fabric:fabric-language-kotlin:${rpp["fabric-language-kotlin"]}"

val Project.`mod-menu`: String get() = "com.terraformersmc:modmenu:${rpp["mod-menu"]}"
val Project.`emi`: String get() = "dev.emi:emi:${rpp["emi"]}"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Forge

val Project.`forge`: String get() = "net.minecraftforge:forge:${`minecraft-version`}-${rpp["forge"]}"
val Project.`kotlin-for-forge`: String get() = "thedarkcolour:kotlinforforge:${rpp["kotlin-for-forge"]}"

val Project.`architectury-forge`: String get() = "dev.architectury:architectury-forge:${rpp["architectury"]}"
val Project.`rei-forge`: String get() = "me.shedaniel:RoughlyEnoughItems-forge:${rpp["rei"]}"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// License

val Project.`license-title`: String get() = "${rpp["license-title"]}"
val Project.`license-year`: String get() = "${rpp["license-year"]}"
val Project.`license-name`: String get() = "${rpp["license-name"]}"
