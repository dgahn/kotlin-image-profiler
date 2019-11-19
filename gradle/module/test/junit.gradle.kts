object Versions {
    const val junitPlatform = "1.5.2"
    const val assertj = "3.14.0"
}

object Dependencies {
    const val platformRunner = "org.junit.platform:junit-platform-runner:${Versions.junitPlatform}"
    const val assertjCore = "org.assertj:assertj-core:${Versions.assertj}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.platformRunner)
    testImplementation(Dependencies.assertjCore)
}
