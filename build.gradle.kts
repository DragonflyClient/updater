plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
}

group = "net.inceptioncloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.formdev:flatlaf:0.37")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("net.lingala.zip4j:zip4j:2.6.1")

    testImplementation("junit", "junit", "4.12")
    implementation(kotlin("script-runtime"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "updater.DragonflyUpdater"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}