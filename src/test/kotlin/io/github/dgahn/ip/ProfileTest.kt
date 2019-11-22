package io.github.dgahn.ip

import com.nhaarman.mockitokotlin2.mock
import io.github.dgahn.ip.profile.Profile
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.profile.embedded.Statistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


object ProfileTest {

    private val histogram = mock<Histogram>()
    private val metaInfo = mock<MetaInfo>()
    private val statistics = mock<Statistics>()

    @Test
    fun `Profile는 id가 null인 Profile을 인스턴스화 할 수 있다`() {
        val profile = Profile(null, metaInfo, statistics, histogram)

        assertThat(profile.id).isEqualTo(null)
    }

}
