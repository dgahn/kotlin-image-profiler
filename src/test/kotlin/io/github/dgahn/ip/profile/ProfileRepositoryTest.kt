package io.github.dgahn.ip.profile

import io.github.dgahn.ip.fixture.MainFixture.Companion.nullIdProfiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


object ProfileRepositoryTest {

    private val profileRepository = ProfileRepository()

    @BeforeEach
    fun setUp() {
        profileRepository.removeAll()
    }

    @Test
    fun `findProfileSummaryList()을 호출하면 3개의 Profile 요약 정보를 반환할 수 있다`() {
        // given
        nullIdProfiles.forEach { profileRepository.save(it) }

        // when
        val profiles = profileRepository.findProfileSummaryList()

        // then
        assertThat(profiles).hasSize(3)
    }

    @Test
    fun `save()을 호출하면 Profile에 Id가 부여할 수 있다`() {
        // given
        val profile = nullIdProfiles[0]

        // when
        profileRepository.save(profile)

        // then
        assertThat(profile.id).isNotNull()
    }

    @Test
    fun `findById(1)을 호출하면 Id가 1이고 프로퍼티가 NotNull Profile의 정보를이 반환할 수 있다`() {
        // given
        val profile = nullIdProfiles[0]
        profileRepository.save(profile)
        val id = 1L

        // when
        val findProfile = profileRepository.findById(id)!!

        // then
        assertThat(findProfile.id).isEqualTo(1L)
        assertThat(findProfile.histogram).isNotNull
        assertThat(findProfile.statistics).isNotNull
        assertThat(findProfile.metaInfo).isNotNull
    }

}
