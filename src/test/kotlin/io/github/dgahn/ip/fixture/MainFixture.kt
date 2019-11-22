package io.github.dgahn.ip.fixture

import io.github.dgahn.ip.fixture.RestFixture.Companion.histograms
import io.github.dgahn.ip.profile.Profile
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.profile.embedded.Statistics
import java.time.LocalDateTime


class MainFixture {

    companion object {

        val statistics = Statistics(255.0,0.0, 127.5)
        val metaInfo = MetaInfo("earth.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo1 = MetaInfo("earth1.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo2 = MetaInfo("earth2.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo3 = MetaInfo("earth3.jpg", 1, 1, LocalDateTime.now().toString())

        private val histogram = Histogram(histograms)
        val nullIdProfiles = listOf(
            Profile(null, metaInfo1, statistics, histogram),
            Profile(null, metaInfo2, statistics, histogram),
            Profile(null, metaInfo3, statistics, histogram)
        )

        val profileRegisterDto = ProfileRegisterDto(RestFixture.file, RestFixture.name)

    }
}
