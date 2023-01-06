plugins {
    // The Kotlin DSL plugin sets up Kotlin with the correct version
    // and lets me do .gradle.kts files as plugins.
    // See https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin-dsl_plugin
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm-tree:9.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
