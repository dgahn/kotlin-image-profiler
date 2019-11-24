package io.github.dgahn.ip.profile

import akka.actor.testkit.typed.javadsl.TestKitJunitResource
import akka.actor.typed.ActorRef
import akka.http.javadsl.model.HttpEntities
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.MediaTypes
import akka.http.javadsl.model.Multiparts
import akka.http.javadsl.testkit.JUnitRouteTest
import akka.http.javadsl.testkit.TestRoute
import akka.http.javadsl.testkit.TestRouteResult
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.dgahn.ip.fixture.MainFixture
import io.github.dgahn.ip.fixture.RestFixture
import io.github.dgahn.ip.fixture.RestFixture.Companion
import io.github.dgahn.ip.profile.actor.HistogramMaker
import io.github.dgahn.ip.profile.actor.HistogramMaker.HistogramCommand
import io.github.dgahn.ip.profile.actor.MetaInfoMaker
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MetaInfoCommand
import io.github.dgahn.ip.profile.actor.StatisticsMaker
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCommand
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ProfileRouteTest : JUnitRouteTest() {

    val profileRepository = ProfileRepository()
    val objectMapper = ObjectMapper()
    lateinit var appRoute: TestRoute


    companion object {
        @JvmField @ClassRule
        val testKit = TestKitJunitResource()

        lateinit var metaInfoMaker: ActorRef<MetaInfoCommand>
        lateinit var histogramMaker: ActorRef<HistogramCommand>
        lateinit var statisticsMaker: ActorRef<StatisticsCommand>

        @BeforeClass @JvmStatic
        fun beforeClass() {
            metaInfoMaker = testKit.spawn(MetaInfoMaker.create(), "meta-info");
            histogramMaker = testKit.spawn(HistogramMaker.create(), "histogram");
            statisticsMaker = testKit.spawn(StatisticsMaker.create(), "statistics");
        }
    }

    @Before
    fun before() {
        val profileRoute = ProfileRoute(
            testKit.system(),
            profileRepository,
            metaInfoMaker,
            histogramMaker,
            statisticsMaker
        )

        appRoute = testRoute(profileRoute.createRoute());
    }

    @Test
    fun `Profile 목록 조회를 하면 3개의 Profile 요약 정보를 반환한다`() {
        // given
        MainFixture.nullIdProfiles.forEach { profileRepository.save(it) }

        // when
        val run = appRoute.run(HttpRequest.GET("/profile"));

        // then
        run.assertStatusCode(200);
        run.assertEntity(objectMapper.writeValueAsString(MainFixture.summaryProfiles))
    }

    @Test
    fun `Profile 목록 조회를 조회하는데 Profile 목록이 없으면 204를 반환한다`() {
        // when
        val run = appRoute.run(HttpRequest.GET("/profile"));

        // then
        run.assertStatusCode(204)
    }

    @Test
    fun `Id가 1L인 Profile을 조회할 수 있다`() {
        // given
        profileRepository.save(MainFixture.nullIdProfile)

        // when
        val run = appRoute.run(HttpRequest.GET("/profile/1"));


        // then
        run.assertStatusCode(200);
        run.assertEntity(objectMapper.writeValueAsString(MainFixture.profile))
    }

    @Test
    fun `Id가 1L인 Profile이 존재하지 않으면 204를 반환한다`() {
        // when
        val run = appRoute.run(HttpRequest.GET("/profile/1"))

        // then
        run.assertStatusCode(204)
    }

    @Test
    fun `JPEG 형식의 이미지파일을 profile로 저장하면 201을 반환한다`() {
        // given
        val map = mapOf("filename" to "earth.jpg")
        val file = RestFixture.img

        val imgBodyPart = Multiparts.createFormDataBodyPart(
            "img", HttpEntities.create(MediaTypes.IMAGE_JPEG.toContentType(), file), map
        );
        val nameBodyPart = Multiparts.createFormDataBodyPart(
            "name", HttpEntities.create(map.get("filename"))
        )
        val formData = Multiparts.createFormDataFromParts(
            imgBodyPart,
            nameBodyPart
        );

        // when
        val run = appRoute.run (HttpRequest.POST("/profile").withEntity(formData.toEntity()));

        // then
        run.assertStatusCode(201);
        run.assertEntity("Resource Created.");
    }

}
