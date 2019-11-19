object Versions {
    const val spek2 = "2.0.8"
}

object Dependencies {
    const val dslJvm = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek2}"
    const val runnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:${Versions.spek2}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.dslJvm)
}

val testRuntimeOnly by configurations
dependencies {
    testRuntimeOnly(Dependencies.runnerJunit5)
}
