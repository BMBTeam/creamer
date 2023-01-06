plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}
