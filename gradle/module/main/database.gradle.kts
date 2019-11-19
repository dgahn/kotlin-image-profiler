object Versions {
    const val h2 = "1.4.197"
    const val hibernate = "5.4.9.Final"
}

object Dependencies {
    const val h2database = "com.h2database:h2:${Versions.h2}"
    const val entityManager = "org.hibernate:hibernate-entitymanager:${Versions.hibernate}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.h2database)
    implementation(Dependencies.entityManager)
}
