enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ResourcefulLib"

pluginManagement {
    repositories {
        // TODO: remove
        maven {
            name = "Maven for PR #3198" // https://github.com/neoforged/NeoForge/pull/3198
            url = uri("https://prmaven.neoforged.net/NeoForge/pr3198")
            content {
                includeModule("net.neoforged", "neoforge")
                includeModule("net.neoforged", "testframework")
            }
        }

        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.teamresourceful.com/repository/maven-public/")
        gradlePluginPortal()
        mavenLocal()
    }
}

// TODO: remove
dependencyResolutionManagement {
    repositories {
        maven {
            name = "Maven for PR #3198" // https://github.com/neoforged/NeoForge/pull/3198
            url = uri("https://prmaven.neoforged.net/NeoForge/pr3198")
            content {
                includeModule("net.neoforged", "neoforge")
                includeModule("net.neoforged", "testframework")
            }
        }
    }
}

plugins {
    id("com.teamresourceful.resourcefulsettings") version "0.0.6"
}

include("common")
include("fabric")
include("neoforge")