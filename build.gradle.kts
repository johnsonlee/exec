import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project.DEFAULT_VERSION

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    kotlin("kapt") version embeddedKotlinVersion
    id("io.johnsonlee.sonatype-publish-plugin") version "1.10.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":fetch"))
    implementation(project(":json2csv"))
    implementation(project(":save-cookies"))
}

allprojects {
    group = "io.johnsonlee.exec"
    version = project.findProperty("version")?.takeIf { it != DEFAULT_VERSION } ?: "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        google()
    }

    val mainClassName = extra["main.class"] as String

    plugins.withId("com.github.johnrengelman.shadow") {
        val shadowJar by tasks.getting(ShadowJar::class) {
            archiveBaseName.set(project.name)
            archiveClassifier.set("all")
            archiveVersion.set("${project.version}")
            manifest {
                attributes["Main-Class"] = mainClassName
            }
            mergeServiceFiles()
        }

        val buildExecutable by tasks.registering {
            group = "distribution"
            description = "Build self-contained executable CLI"

            doLast {
                layout.buildDirectory.get().dir("executable").dir(project.name).asFile.apply {
                    parentFile.mkdirs()
                    outputStream().use { out ->
                        rootProject.file("launcher.sh").inputStream().copyTo(out)
                        shadowJar.archiveFile.get().asFile.inputStream().copyTo(out)
                    }
                    setExecutable(true, false)
                }
            }
        }

        shadowJar.finalizedBy(buildExecutable)
    }
}
