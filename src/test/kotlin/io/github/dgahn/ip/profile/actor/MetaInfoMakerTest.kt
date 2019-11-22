package io.github.dgahn.ip.profile.actor

import akka.actor.testkit.typed.javadsl.TestKitJunitResource
import io.github.dgahn.ip.fixture.MainFixture
import io.github.dgahn.ip.profile.embedded.MetaInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class MetaInfoMakerTest {

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
    fun `MetaInfoMaker Actor에 MakeMetaInfo 메시지를 전송하면 MetaInfo 메시지를 반환 받을 수 있다`() {
        // given
        val probe = testKit.createTestProbe(MetaInfo::class.java)
        val makeMetaInfoMaker = testKit.spawn(MetaInfoMaker.create(MainFixture.profileRegisterDto))

        // when
        makeMetaInfoMaker.tell(MetaInfoMaker.MakeMetaInfo(probe.ref))

        // then
        val receiveMessage = probe.receiveMessage()
        assertThat(receiveMessage.name).isEqualTo(MainFixture.metaInfo.name)
    }

}
