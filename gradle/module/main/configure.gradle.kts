object Versions {
    const val jackson = "2.10.1"
    const val guava = "28.1-jre"
}

object Dependencies {
    const val databind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.databind)
    implementation(Dependencies.guava)
}
