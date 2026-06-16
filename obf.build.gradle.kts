import java.io.BufferedReader
import java.io.FileReader
import org.gradle.jvm.tasks.Jar

plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    id("maven-publish")
    id("com.modrinth.minotaur") version "2.+"
    kotlin("jvm") version "2.2.10"
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

version = "${project.property("mod_version")}+${stonecutter.current.project}"
group = project.property("maven_group") as String

base.archivesName = project.property("archives_base_name") as String

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
    maven("https://api.modrinth.com/maven")
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${stonecutter.current.project}")
	mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    modLocalRuntime("maven.modrinth:sodium:${property("deps.sodium_version")}")
    modLocalRuntime("maven.modrinth:modmenu:${property("deps.modmenu_version")}")
}

fletchingTable {
    mixins.create("main") {
        // Default matches the default value in the annotation
        mixin("default", "${project.property("archives_base_name")}.mixins.json")
    }
    mixins.all {
        automatic = true
    }
}

tasks.processResources {
    val modVersion = project.version
    val minecraftVersion = stonecutter.current.version
    inputs.property("version", modVersion)
    inputs.property("minecraft", minecraftVersion)

    println("Mod version is $modVersion")

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to modVersion,
                "minecraft" to minecraftVersion
            )
        )
    }
}

tasks.named("build") {
    finalizedBy("autoVersionChangelog")
}

tasks.register("autoVersionChangelog") {
    doLast {
        val changelog = File("changelog.md")
        val reader = BufferedReader(FileReader(changelog))
        val lines = reader.readLines().toMutableList()
        val title = "Laseredstone ${project.property("mod_version")}"
        lines[0] = title
        changelog.bufferedWriter().use { writer ->
            for (i in 0..<lines.size) {
                writer.write(lines[i])
                if (i != lines.size - 1) {
                    writer.newLine()
                }
            }
        }
        println("Changelog header successfully replaced as $title")
    }
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }

    runConfigs["client"].apply {
        programArgs("--username=Survivalblock", "--uuid=c45e97e6-94ef-42da-8b5e-0c3209551c3f")
    }

    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    val java = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17

    targetCompatibility = java
    sourceCompatibility = java
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)

    from("LICENSE") {
        rename { "${it}_${base.archivesName}"}
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}

modrinth {
	token = providers.environmentVariable("MODRINTH_TOKEN")
	projectId = project.base.archivesName
	version = project.version
    uploadFile.set(tasks.named<Jar>("remapJar").get().archiveFile)
	gameVersions.addAll("${project.property("deps.compatibleVersions")}".split(", ").toList())
	loaders.addAll("${project.property("deps.compatibleLoaders")}".split(", ").toList())
	changelog = rootProject.file("changelog.md").readText()
	syncBodyFrom = "<!--DO NOT EDIT MANUALLY: synced from gh readme-->\n" + rootProject.file("README.md").readText()
}
