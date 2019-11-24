plugins {
    java
    kotlin("jvm") version "1.3.60"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.60"
    application
}

group = "io.github.dgahn"
version = "1.0.0"

repositories {
    mavenCentral()
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Wrapper> {
    gradleVersion = "6.0"
    distributionType = Wrapper.DistributionType.BIN
}

apply {
    from("gradle/module/main/all-deps.gradle.kts")
    from("gradle/module/test/all-deps.gradle.kts")
}

application {
    mainClassName = "io.github.dgahn.ip.ImageProfilerApplicationKt"
}