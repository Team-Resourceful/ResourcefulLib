import com.teamresourceful.publishing.GitHubPom
import com.teamresourceful.publishing.javaPublishing
import com.teamresourceful.utils.Platform
import com.teamresourceful.utils.getPlatform

plugins {
    java
    id("maven-publish")
    alias(libs.plugins.resourceful.gradle)
    alias(libs.plugins.resourceful.minecraft) apply false
}

subprojects {
    apply(plugin = "maven-publish")

    version = rootProject.libs.versions.mod.version.get()

    val platform = getPlatform()

    when (platform) {
        Platform.COMMON -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-common")
        Platform.FABRIC -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-fabric")
        Platform.NEOFORGE -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-neoforge")
    }

    if (platform != Platform.COMMON) {
        tasks.withType<JavaCompile> {
            val serviceArgs = listOf(
                "-Xplugin:ServicePlugin",
                "--service-plugin-platform=$platform",
                "--service-plugin-platform-class=com.teamresourceful.resourcefullib.common.lib.Platform",
            )

            options.encoding = "UTF-8"
            options.compilerArgs.add(serviceArgs.joinToString(separator = " "))
        }
    }

    repositories {
        maven("https://prmaven.neoforged.net/NeoForge/pr2879")
    }

    dependencies {
        if (platform == Platform.COMMON) {
            "api"(rootProject.libs.yabn)
            "api"(rootProject.libs.bytecodecs)
        } else {
            annotationProcessor(rootProject.libs.service.plugin)

            implementation(rootProject.libs.yabn)
            implementation(rootProject.libs.bytecodecs) {
                isTransitive = false
            }

            if (platform == Platform.FABRIC) {
                "include"(rootProject.libs.yabn)
                "include"(rootProject.libs.bytecodecs)
            } else if (platform == Platform.NEOFORGE) {
                "jarJar"(rootProject.libs.yabn)
                "jarJar"(rootProject.libs.bytecodecs)
            }
        }
    }

    javaPublishing {
        artifactId = "${rootProject.name}-${platform.name}-${rootProject.libs.versions.minecraft.get()}".lowercase()

        pom = GitHubPom(
            "ResourcefulLib",
            "The library behind Team Resourceful mods and more.",
            "MIT",
            "https://github.com/Team-Resourceful/ResourcefulLib"
        )

        repo = "https://maven.teamresourceful.com/repository/maven-releases/"
    }
}


resourcefulGradle {
    templates {
        register("readme") {
            source = file("templates/README.md.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
            )
        }
        register("discord") {
            source = file("templates/embed.json.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
                "neoforge" to libs.versions.neoforge.get(),
                "fabric" to libs.versions.fabric.api.get(),
                "fabric_link" to System.getenv("FABRIC_RELEASE_URL"),
                "neoforge_link" to System.getenv("FORGE_RELEASE_URL"),
            )
        }
    }
}
