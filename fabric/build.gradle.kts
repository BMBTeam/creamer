plugins {
    id("bmb.kokos.service-inline")
}

architectury {
    // Create the IDE launch configurations for this subproject.
    platformSetupLoomIde()
    // Set up Architectury for Fabric.
    fabric()
}

// The files below are for using the access widener for the xplat project.
// We need to copy the file from xplat resources to fabric resource
// for Fabric Loader to find it and not crash.

// The generated resources directory for the AW.
val generatedResources = file("src/generated/resources")
// The path to the AW file in the xplat subproject.
val accessWidenerFile = project(":xplat").file("src/main/resources/kokos.accesswidener")

loom {
    // Make the Fabric project use the xplat access widener.
    // Technically useless, But this file is also needed at
    // dev runtime of course.
    accessWidenerPath.set(accessWidenerFile)
}

sourceSets {
    main {
        resources {
            // Mark the AW generated resource directory as
            // a source directory for the resources of
            // the "main" source set.
            srcDir(generatedResources)
        }
    }
}

// Set up various Maven repositories for mod compat.
repositories {
    // TerraformersMC maven for Mod Menu and EMI.
    maven("https://maven.terraformersmc.com/releases")
}

dependencies {
    // Depend on the xplat project. The "namedElements" configuration contains
    // the non-remapped classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing"
    // configurations like apiElements.
    implementation(project(":xplat", "namedElements")) { isTransitive = false }
    // Bundle the transformed version of the xplat project in the mod.
    // The transformed version includes things like fixed refmaps.
    bundle(project(":xplat", "transformProductionFabric")) { isTransitive = false }

    // Standard Fabric mod setup.
    modImplementation(`fabric-loader`)
    modImplementation(`fabric-api`)
    modApi(`fabric-language-kotlin`)

    // Mod compat.
    modLocalRuntime(modCompileOnly(`mod-menu`)!!)
    modLocalRuntime(modCompileOnly(emi) { isTransitive = false })
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    // This task simply copies it.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResources)
    }

    processResources {
        // Hook the AW copying to processResources.
        dependsOn(copyAccessWidener)
        inputs.properties(replacementTemplates)
        filesMatching("fabric.mod.json") { expand(replacementTemplates) }
    }
}
