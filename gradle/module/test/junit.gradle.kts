object Versions {
    const val junitPlatform = "1.5.2"
    const val junitJupiter = "5.5.2"
    const val assertj = "3.14.0"
}

object Dependencies {
    const val assertjCore = "org.assertj:assertj-core:${Versions.assertj}"
    const val platformRunner = "org.junit.platform:junit-platform-runner:${Versions.junitPlatform}"
    const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiter}"
    const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junitJupiter}"
    const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiter}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.assertjCore)
    testImplementation(Dependencies.platformRunner)
    testImplementation(Dependencies.jupiterApi)
    testImplementation(Dependencies.jupiterParams)
    testImplementation(Dependencies.jupiterEngine)
}
