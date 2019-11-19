object Versions {
    const val akka = "2.6.0"
    const val akkaHttp = "10.1.10"
}

object Dependencies {
    const val actorTestkit = "com.typesafe.akka:akka-actor-testkit-typed_2.13:${Versions.akka}"
    const val httpTestkit = "com.typesafe.akka:akka-http-testkit_2.13:${Versions.akkaHttp}"
}

val testImplementation by configurations
dependencies {
    testImplementation(Dependencies.actorTestkit)
    testImplementation(Dependencies.httpTestkit)
}
