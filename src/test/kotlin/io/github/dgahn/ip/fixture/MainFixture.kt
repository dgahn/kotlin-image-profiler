package io.github.dgahn.ip.fixture

import io.github.dgahn.ip.fixture.RestFixture.Companion.histograms
import io.github.dgahn.ip.profile.Profile
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.ProfileSummaryDto
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.profile.embedded.Statistics
import java.time.LocalDateTime


class MainFixture {

    companion object {

        val statistics = Statistics(255, 255, 255, 0, 0, 0, 46.0, 57.0, 51.0)
        val metaInfo = MetaInfo("earth.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo1 = MetaInfo("earth1.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo2 = MetaInfo("earth2.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo3 = MetaInfo("earth3.jpg", 1, 1, LocalDateTime.now().toString())
        private val metaInfo4 = MetaInfo("earth4.jpg", 1, 1, LocalDateTime.now().toString())

        private val histogram = Histogram(histograms)

        @JvmStatic
        val nullIdProfiles = listOf(
            Profile(null, metaInfo1, statistics, histogram),
            Profile(null, metaInfo2, statistics, histogram),
            Profile(null, metaInfo3, statistics, histogram)
        )

        @JvmStatic
        val profiles = listOf(
            Profile(1L, metaInfo1, statistics, histogram),
            Profile(2L, metaInfo2, statistics, histogram),
            Profile(3L, metaInfo3, statistics, histogram)
        )

        @JvmStatic
        val nullIdProfile = Profile(null, metaInfo4, statistics, histogram)

        @JvmStatic
        val profile = Profile(1L, metaInfo4, statistics, histogram)

        @JvmStatic
        val summaryProfiles = listOf(
            ProfileSummaryDto(1, "earth1.jpg"),
            ProfileSummaryDto(2, "earth2.jpg"),
            ProfileSummaryDto(3, "earth3.jpg")
        )

        val profileRegisterDto = ProfileRegisterDto(RestFixture.img, RestFixture.name)

    }
}
