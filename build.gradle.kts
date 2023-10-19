import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.3.8"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.teamvoided.iridium") version "3.0.2"
}

group = project.properties["maven_group"]!!
version = project.properties["mod_version"]!!
base.archivesName.set(project.properties["archives_base_name"] as String)
description = "ahahzahahahahahahhah"

repositories {
    mavenCentral()
    maven {
        name = "brokenfuseReleases"
        url = uri("https://maven.teamvoided.org/releases")
    }
}

modSettings {
    modId(base.archivesName.get())
    modName("Soulforged")

    entrypoint("main", "org.teamvoided.soulforged.Soulforged::commonInit")
    entrypoint("client", "org.teamvoided.soulforged.Soulforged::clientInit")
}

dependencies{
   modImplementation("org.teamvoided:voidlib-core:1.5.7+1.20.1")
}

tasks {
    val targetJavaVersion = 17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }
}