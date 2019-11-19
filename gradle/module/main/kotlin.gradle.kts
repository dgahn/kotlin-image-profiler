object Versions {
    const val kotlin = "1.3.60"
}

object Dependencies {
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val noarg = "org.jetbrains.kotlin:kotlin-noarg:${Versions.kotlin}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.reflect)
    implementation(Dependencies.stdLib)
    implementation(Dependencies.noarg)
}
