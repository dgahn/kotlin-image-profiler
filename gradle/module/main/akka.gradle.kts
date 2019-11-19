object Versions {
    const val akka = "2.6.0"
    const val akkaHttp = "10.1.10"
}

object Dependencies {
    const val stream = "com.typesafe.akka:akka-stream_2.13:${Versions.akka}"
    const val actorTyped = "com.typesafe.akka:akka-actor-typed_2.13:${Versions.akka}"
    const val http = "com.typesafe.akka:akka-http_2.13:${Versions.akkaHttp}"
    const val jackson = "com.typesafe.akka:akka-http-jackson_2.13:${Versions.akkaHttp}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.stream)
    implementation(Dependencies.actorTyped)
    implementation(Dependencies.http)
    implementation(Dependencies.jackson)
}
