enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ResourcefulLib"

pluginManagement {
    repositories {
        maven("https://maven.teamresourceful.com/repository/maven-private/") // TODO remove when arch stops breaking 1.21.5
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.teamresourceful.com/repository/maven-public/")
        gradlePluginPortal()
    }
}

plugins {
    id("com.teamresourceful.resourcefulsettings") version "0.0.6"
}

include("common")
include("fabric")
include("neoforge")