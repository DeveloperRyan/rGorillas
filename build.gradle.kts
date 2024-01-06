plugins {
    kotlin("jvm") version "1.8.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://repo.powbot.org/releases")
    }
}

dependencies {
    implementation("org.powbot:client-sdk:1.0.43-SNAPSHOT")
    implementation("org.powbot:client-sdk-loader:1.+")
    implementation("com.google.guava:guava:31.1-jre") // needed for @Subscribe annotations / event bus
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
}

kotlin {
    jvmToolchain(11)
}