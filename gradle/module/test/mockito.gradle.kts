object Versions {
    const val mockito = "3.1.0"
    const val mockitoKotlin2 = "2.2.0"
}

object Dependencies {
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoJunit = "org.mockito:mockito-junit-jupiter:${Versions.mockito}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockito}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin2}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.mockitoCore)
    testImplementation(Dependencies.mockitoJunit)
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.mockitoKotlin)
}
