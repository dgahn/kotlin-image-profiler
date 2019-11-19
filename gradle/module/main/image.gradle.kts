object Versions {
    const val imagej = "1.52r"
    const val metadataExtractor = "2.12.0"
}

object Dependencies {
    const val imagej = "net.imagej:ij:${Versions.imagej}"
    const val metadataExtractor = "com.drewnoakes:metadata-extractor:${Versions.metadataExtractor}"
}

val implementation by configurations
dependencies {
    implementation(Dependencies.imagej)
    implementation(Dependencies.metadataExtractor)
}
