package io.github.dgahn.ip.profile;


import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.typed.ActorRef;
import akka.http.javadsl.model.*;
import akka.http.javadsl.model.Multipart.FormData;
import akka.http.javadsl.model.Multipart.FormData.BodyPart;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import akka.http.javadsl.testkit.TestRouteResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dgahn.ip.fixture.MainFixture;
import io.github.dgahn.ip.fixture.RestFixture;
import io.github.dgahn.ip.profile.actor.HistogramMaker;
import io.github.dgahn.ip.profile.actor.HistogramMaker.HistogramCommand;
import io.github.dgahn.ip.profile.actor.MetaInfoMaker;
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MetaInfoCommand;
import io.github.dgahn.ip.profile.actor.StatisticsMaker;
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCommand;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfileRouteTest extends JUnitRouteTest {

    @ClassRule
    public static TestKitJunitResource testkit = new TestKitJunitResource();

    private ProfileRepository profileRepository = new ProfileRepository();

    private TestRoute appRoute;
    private static ActorRef<MetaInfoCommand> metaInfoMaker;
    private static ActorRef<HistogramCommand> histogramMaker;
    private static ActorRef<StatisticsCommand> statisticsMaker;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeClass
    public static void beforeClass() {
        metaInfoMaker = testkit.spawn(MetaInfoMaker.create(), "meta-info");
        histogramMaker = testkit.spawn(HistogramMaker.create(), "histogram");
        statisticsMaker = testkit.spawn(StatisticsMaker.create(), "statistics");
    }

    @Before
    public void before() {
        final ProfileRoute profileRoute = new ProfileRoute(
                testkit.system(),
                profileRepository,
                metaInfoMaker,
                histogramMaker,
                statisticsMaker
        );

        appRoute = testRoute(profileRoute.createRoute());
    }

    @Test
    public void Profile_목록_조회를_하면_3개의_Profile_요약_정보를_반환한다() throws JsonProcessingException {
        // given
        MainFixture.getNullIdProfiles().forEach(e -> profileRepository.save(e));

        // when
        TestRouteResult run = appRoute.run(HttpRequest.GET("/profile"));

        // then
        run.assertStatusCode(200);
        run.assertEntity(objectMapper.writeValueAsString(MainFixture.getSummaryProfiles()));
    }

    @Test
    public void Profile_목록_조회를_조회하는데_Profile_목록이_없으면_204를_반환한다() {
        // when
        TestRouteResult run = appRoute.run(HttpRequest.GET("/profile"));

        // then
        run.assertStatusCode(204);
    }

    @Test
    public void Id가_1L인_Profile을_조회할_수_있다() throws JsonProcessingException {
        // given
        profileRepository.save(MainFixture.getNullIdProfile());

        // when
        TestRouteResult run = appRoute.run(HttpRequest.GET("/profile/1"));


        // then
        run.assertStatusCode(200);
        run.assertEntity(objectMapper.writeValueAsString(MainFixture.getProfile()));
    }

    @Test
    public void Id가_1L인_Profile이_존재하지_않으면_204를_반환한다() {
        // when
        TestRouteResult run = appRoute.run(HttpRequest.GET("/profile/1"));

        // then
        run.assertStatusCode(204);
    }

    @Test
    public void JPEG_형식의_이미지파일을_profile로_저장하면_201을_반환한다() {
        // given
        final Map<String, String> map = new HashMap<>();
        map.put("filename", "earth.jpg");
        File file = RestFixture.getImg();

        final BodyPart imgBodyPart = Multiparts.createFormDataBodyPart(
                "img", HttpEntities.create(MediaTypes.IMAGE_JPEG.toContentType(), file), map
        );
        final BodyPart nameBodyPart = Multiparts.createFormDataBodyPart(
                "name", HttpEntities.create(map.get("filename"))
        );
        final FormData formData = Multiparts.createFormDataFromParts(
                imgBodyPart,
                nameBodyPart
        );

        // when
        TestRouteResult run = appRoute.run(HttpRequest.POST("/profile").withEntity(formData.toEntity()));

        // then
        run.assertStatusCode(201);
        run.assertEntity("Resource Created.");
    }

}
