package io.github.dgahn.ip.profile.actor

import akka.actor.testkit.typed.javadsl.TestKitJunitResource
import io.github.dgahn.ip.fixture.MainFixture
import io.github.dgahn.ip.fixture.RestFixture
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.profile.embedded.Statistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class StatisticsMakerTest {

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
    fun `StatisticsMaker Actor에 MakeStatistics 메시지를 전송하면 Statistics 메시지를 반환 받을 수 있다`() {
        // given
        val probe = testKit.createTestProbe(Statistics::class.java)
        val makeStatistics = testKit.spawn(StatisticsMaker.create(MainFixture.profileRegisterDto))

        // when
        makeStatistics.tell(StatisticsMaker.MakeStatistics(probe.ref))

        // then
        val receiveMessage = probe.receiveMessage()
        assertThat(receiveMessage).isEqualTo(MainFixture.statistics)
    }

}
