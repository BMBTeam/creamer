pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") // Fabric
        maven("https://maven.architectury.dev/") // Arch
        maven("https://maven.minecraftforge.net/") // Forge
        gradlePluginPortal()
    }
}

if (JavaVersion.current().ordinal + 1 < 17) throw IllegalStateException("Please run Gradle with Java17+!")

rootProject.name = "kokos"

includeBuild("building")
include("xplat", "fabric", "forge")
