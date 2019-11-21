package io.github.dgahn.ip.fixture

import io.github.dgahn.ip.fixture.RestFixture.Companion.histograms
import io.github.dgahn.ip.profile.Profile
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.Metadata
import io.github.dgahn.ip.profile.embedded.Statistics
import java.time.LocalDateTime

class ObjectFixture {

    companion object {
        private val statistics = Statistics(127.5, 0.0, 255.0)
        private val metadata1 = Metadata("earth1.jpg", 1, 1, LocalDateTime.now().toString())
        private val metadata2 = Metadata("earth2.jpg", 1, 1, LocalDateTime.now().toString())
        private val metadata3 = Metadata("earth3.jpg", 1, 1, LocalDateTime.now().toString())

        private val histogram = Histogram(histograms)
        val nullIdProfiles = listOf(
            Profile(null, metadata1, statistics, histogram),
            Profile(null, metadata2, statistics, histogram),
            Profile(null, metadata3, statistics, histogram)
        )

    }
}