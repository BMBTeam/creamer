architectury {
    // Create the IDE launch configurations for this subproject.
    platformSetupLoomIde()
    // Set up Architectury for Forge.
    forge()
}

loom {
    // Make the Forge project use the xplat access widener.
    accessWidenerPath.set(project(":xplat").file("src/main/resources/kokos.accesswidener"))

    forge {
        // Enable Loom's AW -> AT conversion for Forge.
        // This will make remapJar convert the xplat AW
        // to a Forge access transformer.
        convertAccessWideners.set(true)
        // Add an "extra" converted access widener which
        // comes from outside the project.
        // The path is relative to the mod jar's root.
        extraAccessWideners.add("kokos.accesswidener")
        // Add mixin configs. Forge needs these explicitly declared
        // via Gradle; in Fabric, they're in fabric.mod.json.
        mixinConfigs("kokos.xplat.mixins.json", "kokos.forge.mixins.json")
    }
}

repositories {
    // Set up Kotlin for Forge's Maven repository.
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    // Add dependency on Forge. This is mainly used for generating
    // the patched Minecraft jar with Forge classes.
    forge(forge)
    // Add Kotlin for Forge.
    // Based on their own instructions:
    // https://github.com/thedarkcolour/KotlinForForge/blob/site/thedarkcolour/kotlinforforge/gradle/kff-3.8.0-architectury.gradle
    implementation(`kotlin-for-forge`)
    forgeRuntimeLibrary(kotlin("stdlib-jdk8", "1.8.0"))
    forgeRuntimeLibrary(kotlin("reflect", "1.8.0"))
    forgeRuntimeLibrary(`kotlinx-serialization-json`)
    forgeRuntimeLibrary(`kotlinx-coroutines-core`)
    forgeRuntimeLibrary(`kotlinx-coroutines-jdk8`)

    // Depend on the xplat project. The "namedElements" configuration contains
    // the non-remapped classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing"
    // configurations like apiElements.
    implementation(project(":xplat", "namedElements")) { isTransitive = false }
    // Bundle the transformed version of the xplat project in the mod.
    // The transformed version includes things like fixed refmaps.
    bundle(project(":xplat", "transformProductionForge")) { isTransitive = false }

    // Add regular mod dependency on REI - API for compile time
    // and the mod itself for runtime. modLocalRuntime won't be exposed
    // if other mods depend on your mod unlike modRuntimeOnly.
    // TODO: Revert back to API jar after it's fixed:
    //  https://github.com/shedaniel/RoughlyEnoughItems/issues/1194
    modLocalRuntime(`architectury-forge`)
    modLocalRuntime(modCompileOnly(`rei-forge`)!!)
}

tasks {
    processResources {
        inputs.properties(replacementTemplates)
        filesMatching("META-INF/mods.toml") { expand(replacementTemplates) }
    }
}
