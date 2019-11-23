object Versions {
    const val spek = "2.0.8"
}

object Dependencies {
    const val spekDsl = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.spekDsl)
}