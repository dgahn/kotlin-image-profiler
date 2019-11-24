package io.github.dgahn.ip.profile

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Scheduler
import akka.actor.typed.javadsl.AskPattern
import akka.http.javadsl.server.AllDirectives
import akka.http.javadsl.server.PathMatchers
import akka.http.javadsl.server.RejectionHandler
import akka.http.javadsl.server.Route
import akka.http.javadsl.server.directives.FileInfo
import akka.japi.function.Function
import io.github.dgahn.ip.profile.actor.HistogramMaker
import io.github.dgahn.ip.profile.actor.HistogramMaker.HistogramCommand
import io.github.dgahn.ip.profile.actor.HistogramMaker.HistogramCompleted
import io.github.dgahn.ip.profile.actor.HistogramMaker.MakeHistogram
import io.github.dgahn.ip.profile.actor.MetaInfoMaker
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MakeMetaInfo
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MetaInfoCommand
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MetaInfoCompleted
import io.github.dgahn.ip.profile.actor.StatisticsMaker
import io.github.dgahn.ip.profile.actor.StatisticsMaker.MakeStatistics
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCommand
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCompleted
import io.github.dgahn.ip.profile.exception.FileReadFailException
import io.github.dgahn.ip.profile.exception.ProfileNotFoundException
import io.github.dgahn.ip.profile.exception.ProfileSaveFailException
import io.github.dgahn.ip.util.HttpResponseUtil
import mu.KotlinLogging
import java.io.File
import java.time.Duration
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit


class ProfileRoute(
    private val system: ActorSystem<*>,
    private val profileRepository: ProfileRepository,
    private val metaInfoMaker: ActorRef<MetaInfoCommand>,
    private val histogramMaker: ActorRef<HistogramCommand>,
    private val statisticsMaker: ActorRef<StatisticsCommand>
) : AllDirectives() {
    private val timeOutOfBoundsException: Long = 5
    private val askTimeout: Duration = Duration.ofSeconds(timeOutOfBoundsException)
    private val duration = scala.concurrent.duration.Duration.create(timeOutOfBoundsException, TimeUnit.SECONDS)
    private val scheduler: Scheduler = system.scheduler()

    private val logger = KotlinLogging.logger {}

    fun createRoute(): Route {
        val defaultHandler = RejectionHandler.defaultHandler();

        return pathPrefix("profile") {
            concat(
                pathEnd {
                    concat(
                        get {
                            getSummaryProfiles()
                        },
                        post {
                            saveProfile()
                        }
                    )
                },
                path(PathMatchers.longSegment()) { id: Long ->
                    get {
                        getProfile(id)
                    }
                }
            )
        }.seal(defaultHandler, HttpResponseUtil.exceptionHandler())
    }

    private fun getSummaryProfiles(): Route {
        val profiles = profileRepository.findProfileSummaryList()
            ?: throw ProfileNotFoundException("There is no subscribed profile.")
        return HttpResponseUtil.responseOK(profiles);
    }

    private fun saveProfile(): Route? {
        return toStrictEntity(duration) {
            formField("name") { name ->
                storeUploadedFile(
                    "img", temporaryDestination
                ) { _, file ->
                    profileRepository.save(makeProfile(ProfileRegisterDto(file, name)))
                        ?: throw ProfileSaveFailException("Failed to save profile.")
                    HttpResponseUtil.responseCreated()
                }
            }
        }
    }

    private fun getProfile(id: Long): Route {
        val profile =
            profileRepository.findById(id) ?: throw ProfileNotFoundException("The Profile With $id doesn't exist.")

        return HttpResponseUtil.responseOK(profile)
    }

    private fun makeProfile(registerDto: ProfileRegisterDto): Profile {
        val metaInfo = makeMetaInfo(registerDto)
            .thenApply { obj: Any? -> MetaInfoMaker.MetaInfoCompleted::class.java.cast(obj) }
        val histogram = makeHistogram(registerDto)
            .thenApply { obj: Any? -> HistogramMaker.HistogramCompleted::class.java.cast(obj) }
        val statistics = makeStatistics(registerDto)
            .thenApply { obj: Any? -> StatisticsMaker.StatisticsCompleted::class.java.cast(obj) }

        return metaInfo.thenCompose<Profile> { m ->
            histogram
                .thenCombine(
                    statistics
                ) { h, s ->
                    Profile(null, m.message, s.message, h.message)
                }
        }.toCompletableFuture().get()
    }

    private fun makeMetaInfo(registerDto: ProfileRegisterDto): CompletionStage<MetaInfoCompleted> {
        return AskPattern.ask<MetaInfoCommand, MetaInfoCompleted>(
            metaInfoMaker,
            Function { ref: ActorRef<MetaInfoCompleted> -> MakeMetaInfo(registerDto, ref) },
            askTimeout,
            scheduler
        )
    }

    private fun makeStatistics(registerDto: ProfileRegisterDto): CompletionStage<StatisticsCompleted> {
        return AskPattern.ask<StatisticsCommand, StatisticsCompleted>(
            statisticsMaker,
            Function { ref: ActorRef<StatisticsCompleted> -> MakeStatistics(registerDto, ref) },
            askTimeout,
            scheduler
        )
    }

    private fun makeHistogram(registerDto: ProfileRegisterDto): CompletionStage<HistogramCompleted> {
        return AskPattern.ask<HistogramCommand, HistogramCompleted>(
            histogramMaker,
            Function { ref: ActorRef<HistogramCompleted> -> MakeHistogram(registerDto, ref) },
            askTimeout,
            scheduler
        )
    }

    private val temporaryDestination = { info: FileInfo ->
        try {
            File.createTempFile(info.fileName, "")
        } catch (e: Exception) {
            throw FileReadFailException("This file cannot be read.")
        }
    }

}
