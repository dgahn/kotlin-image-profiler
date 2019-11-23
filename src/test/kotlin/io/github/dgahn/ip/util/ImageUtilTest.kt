package io.github.dgahn.ip.util

import io.github.dgahn.ip.fixture.RestFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


object ImageUtilTest {

    @Test
    fun `getMetadataMap()을 호출하면 Image Width, Image Height, File Modified Date를 반환할 수 있다`() {
        // given
        val file = RestFixture.img

        // when
        val metadataMap: Map<String, String> = ImageUtil.getMetadataMap(file)!!

        // then
        assertThat(metadataMap).hasSize(22)
        assertThat(metadataMap).containsEntry("Image Height", "1920 pixels")
        assertThat(metadataMap).containsEntry("Image Width", "1873 pixels")
        assertThat(metadataMap).containsKey("File Modified Date")
    }

    @Test
    fun `getImageStatistics()을 호출하면 Statistics(이미지 통계 값)을 반환할 수 있다`() {
        // given
        val file = RestFixture.img

        // when
        val imageStatistics = ImageUtil.getImageStatistics(file)!!

        // then
        assertThat(imageStatistics.histogram()).hasSize(256)
        assertThat(imageStatistics.max).isEqualTo(255.0)
        assertThat(imageStatistics.min).isEqualTo(0.0)
    }

}
