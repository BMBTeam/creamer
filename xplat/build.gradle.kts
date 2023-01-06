architectury {
    // Set up Architectury for the xplat project.
    // This sets up the transformations (@ExpectPlatform etc.) we need
    // for production environments.
    common("fabric", "forge")
}

loom {
    accessWidenerPath.set(file("src/main/resources/kokos.accesswidener"))
}

dependencies {
    // Add dependencies on the required Kotlin modules.
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(`kotlinx-serialization-json`)
    implementation(`kotlinx-coroutines-core`)
    implementation(`kotlinx-coroutines-jdk8`)
    // Just for @Environment and mixin deps :)
    implementation(`fabric-loader`)
    // Add a mod dependency on REI's API for compat code.
    modCompileOnly(rei)
}
