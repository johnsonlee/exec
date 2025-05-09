plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.johnsonlee.sonatype-publish-plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    kapt(libs.auto.service)
    kapt(libs.pico.codegen)

    api(kotlin("stdlib"))
    api(libs.kotlinx.coroutines.core)
    api(libs.auto.service)
    api(libs.pico.cli)
    api(libs.okhttp)
    api(libs.okhttp.urlconnection)

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

