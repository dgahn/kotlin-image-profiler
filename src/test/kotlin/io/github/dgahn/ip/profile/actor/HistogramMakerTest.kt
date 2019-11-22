package io.github.dgahn.ip.profile.actor

import akka.actor.testkit.typed.javadsl.TestKitJunitResource
import io.github.dgahn.ip.fixture.MainFixture
import io.github.dgahn.ip.fixture.RestFixture
import io.github.dgahn.ip.profile.embedded.Histogram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class HistogramMakerTest {

    private lateinit var testKit: TestKitJunitResource

    @BeforeEach
    fun setUp() {
        testKit = TestKitJunitResource()
    }

    @AfterEach
    fun cleanUp() {
        testKit.after()
    }

    @Test
    fun `HistogramMaker Actor에 MakeHistogram 메시지를 전송하면 Histgram 메시지를 반환 받을 수 있다`() {
        // given
        val probe = testKit.createTestProbe(Histogram::class.java)
        val histogramMaker = testKit.spawn(HistogramMaker.create(MainFixture.profileRegisterDto))

        // when
        histogramMaker.tell(HistogramMaker.MakeHistogram(probe.ref))

        // then
        val receiveMessage = probe.receiveMessage()
        assertThat(receiveMessage.values).isEqualTo(RestFixture.histograms)
    }

}
