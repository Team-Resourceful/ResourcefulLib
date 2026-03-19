enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ResourcefulLib"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.teamresourceful.com/repository/maven-public/")
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("com.teamresourceful.resourcefulsettings") version "0.0.6"
}

include("common")
include("fabric")
include("neoforge")