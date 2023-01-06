@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the base plugin which mostly defines useful "build lifecycle"
    // tasks like assemble, check and build. The root project doesn't contain
    // any Java or Kotlin code, so we won't apply those plugins here.
    // Only the assemble task is used in the root project.
    // See https://docs.gradle.org/current/userguide/base_plugin.html.
    base

    // Set up specific versions of the plugins we're using.
    // Note that of all these plugins, only the Architectury plugin and
    // licenser needs to be applied.
    val kotlinVersion = "1.8.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false

    id("architectury-plugin") version "3.4.143"
    id("org.cadixdev.licenser") version "0.6.1"
    id("dev.architectury.loom") version "1.0.305" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

// Set the Minecraft version for Architectury.
architectury {
    minecraft = `minecraft-version`
}

// Set up basic Maven artifact metadata, including the project version
// and archive names.
group = `maven-group`
// Set the project version to be <mod version>+<minecraft version>
// so the Minecraft version is semver build metadata.
// The "mod-version" and "minecraft-version" properties are read
// from gradle.properties.
version = "${`mod-version`}+${`minecraft-version`}"
base.archivesName.set(`mod-id`)

tasks {
    // Register a custom "collect jars" task that copies the Fabric
    // and Forge mod jars into the root project's build/mods. This makes it
    // easier for me to find them for testing and releasing.
    val collectJars by registering(Copy::class) {
        group = `mod-id`
        // Find the remapJar tasks of projects that aren't :xplat
        // (so :fabric and :forge) and depend on them.
        val tasks = subprojects.filter { it.path != ":xplat" }.map { it.tasks.named("remapJar") }
        dependsOn(tasks)
        // Copy the outputs of the tasks...
        from(tasks)
        // ...into build/mods.
        into(buildDir.resolve("mods"))
    }

    // Set up assemble to depend on the collectJars task,
    // so it gets run on gradlew build.
    assemble {
        dependsOn(collectJars)
    }

    // This is for IDEA. If "classes" doesn't exist, it runs
    // "assemble" - which builds the final project jars and is slow - when
    // you press the hammer icon.
    register("classes") { group = `mod-id` }
}

allprojects {
    apply(plugin = "org.cadixdev.licenser")

    license {
        header(rootProject.file("HEADER"))
        properties {
            set("title", `license-title`)
            set("year", `license-year`)
            set("name", `license-name`)
        }
        include("**/*.kt")
        include("**/*.java")
        include("**/*.kts")
        ignoreFailures.set(true)
    }
}

// Do the shared set up for the Minecraft subprojects.
subprojects {
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.serialization")
        plugin("architectury-plugin")
        plugin("dev.architectury.loom")
    }

    // Set Kotlin feature.
    extensions.configure<KotlinJvmProjectExtension> {
        sourceSets.all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }

    // Set Java version.
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    architectury {
        // Disable Architectury's runtime transformer
        // since we don't use it.
        compileOnly()
    }

    // Copy the artifact metadata from the root project.
    group = rootProject.group
    version = rootProject.version
    base.archivesName.set(rootProject.base.archivesName)

    extensions.configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    dependencies {
        // Set the Minecraft dependency. The `minecraft-version` calls read
        // from gradle.properties (and a variety of other sources).
        // Note that the configuration name has to be in quotes (a string)
        // since Loom isn't applied to the root project, and so
        // the Kotlin accessor method for it isn't generated for this file.
        "minecraft"("net.minecraft:minecraft:${`minecraft-version`}")
        // Find the loom extension. Since it's not applied to the root project,
        // we can't access it directly by name in this file.
        val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
        // Set up the layered mappings with Official mapping.
        // The average modder would have "mappings"("net.fabricmc:yarn:...")
        // or "mappings"(loom.officialMojangMappings()).
        "mappings"(loom.officialMojangMappings())
    }

    tasks {
        withType<KotlinCompile> {
            // Set the Kotlin JVM target to match the Java version
            // for all Kotlin compilation tasks.
            kotlinOptions.jvmTarget = "17"
            @Suppress("SpellCheckingInspection")
            kotlinOptions.freeCompilerArgs = listOf(
                // Compile lambdas to invokedynamic.
                "-Xlamdas=indy",
                // Compile interface functions with bodies to default methods.
                "-Xjvm-default=all-compatibility"
            )
        }

        withType<JavaCompile> {
            options.release.set(17)
            options.encoding = "UTF-8"
        }

        // Include the license in the jar files.
        // See the dependencies section above for why this is in quotes.
        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
        }
    }
}

// Set up "platform" subprojects (non-xplat subprojects).
subprojects {
    if (path != ":xplat") {
        // Apply the shadow plugin which lets us include contents
        // of any libraries in our mod jars. Architectury uses it
        // for bundling the common mod code in the platform jars.
        apply(plugin = "com.github.johnrengelman.shadow")

        // Set a different run directory for the server run config,
        // so it won't override client logs/config (or vice versa).
        extensions.configure<LoomGradleExtensionAPI> {
            runConfigs.getByName("server").runDir = "run/server"
            // "main" matches the default Forge mod's name
            with(mods.maybeCreate("main")) {
                fun Project.sourceSets() = extensions.getByName<SourceSetContainer>("sourceSets")
                sourceSet(sourceSets().getByName("main"))
                sourceSet(project(":xplat").sourceSets().getByName("main"))
            }
        }

        // Define the "bundle" configuration which will be included in the shadow jar.
        val bundle by configurations.creating {
            // This configuration is only meant to be resolved to its files but not published in
            // any way, so we set canBeConsumed = false and canBeResolved = true. See
            // https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:resolvable-consumable-configs.
            isCanBeConsumed = false
            isCanBeResolved = true
        }

        tasks {
            "jar"(Jar::class) {
                archiveClassifier.set("dev-slim")
            }

            "shadowJar"(ShadowJar::class) {
                archiveClassifier.set("dev-shadow")
                // Include our bundle configuration in the shadow jar.
                configurations = listOf(bundle)
            }

            "remapJar"(RemapJarTask::class) {
                dependsOn("shadowJar")
                // Replace the remap jar task's input with the shadow jar containing the common classes.
                inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
                // The project name will be "fabric" or "forge", so this will become the classifier/suffix
                // for the jar. For example: kokos-1.0.0-fabric.jar
                archiveClassifier.set(project.name)
            }
        }
    }
}

val licenseFormatAll = task("licenseFormatAll") { group = `mod-id` }
subprojects { licenseFormatAll.dependsOn("$path:licenseFormat") }
