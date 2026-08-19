pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.6"
    id("dev.kikugie.loom-back-compat") version "0.4"
}

rootProject.name = "laseredstone"
loomx.loomVersion = "1.16-SNAPSHOT"

stonecutter {
    kotlinController = true

    // Subproject configuration
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders.forEach {
            version("$version-$it", version).buildscript = "build.$it.gradle.kts"
        }

        match("1.21.8", "fabric")
        match("1.21.10", "fabric")
        match("1.21.11", "fabric")
        match("26.1.2", "fabric")
        vcsVersion = "1.21.8-fabric"
    }
}