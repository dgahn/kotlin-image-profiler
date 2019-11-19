object Versions {
    const val kotlinLogging = "1.7.6"
    const val sl4j = "1.7.29"
    const val log4j = "2.12.1"
}

object Dependencies {
    const val kotlinLogging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"
    const val sl4jApi = "org.slf4j:slf4j-api:${Versions.sl4j}"
    const val log4jImpl = "org.apache.logging.log4j:log4j-slf4j18-impl:${Versions.log4j}"
    const val log4jApi = "org.apache.logging.log4j:log4j-api:${Versions.log4j}"
    const val log4jCore = "org.apache.logging.log4j:log4j-core:${Versions.log4j}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.kotlinLogging)
    implementation(Dependencies.sl4jApi)
    implementation(Dependencies.log4jImpl)
    implementation(Dependencies.log4jApi)
    implementation(Dependencies.log4jCore)
}
