package io.github.dgahn.ip

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Adapter
import akka.actor.typed.javadsl.Behaviors
import akka.http.javadsl.ConnectHttp
import akka.http.javadsl.Http
import akka.http.javadsl.ServerBinding
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.HttpResponse
import akka.http.javadsl.server.Route
import akka.stream.Materializer
import akka.stream.javadsl.Flow
import io.github.dgahn.ip.profile.ProfileRepository
import io.github.dgahn.ip.profile.ProfileRoute
import io.github.dgahn.ip.profile.actor.HistogramMaker
import io.github.dgahn.ip.profile.actor.MetaInfoMaker
import io.github.dgahn.ip.profile.actor.StatisticsMaker
import mu.KotlinLogging
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage


class ImageProfilerApplication {

    private val logger = KotlinLogging.logger {}

    fun run(
        route: Route,
        system: ActorSystem<*>
    ) {
        val classicSystem: akka.actor.ActorSystem = Adapter.toClassic(system)
        val http: Http = Http.get(classicSystem)
        val materializer: Materializer = Materializer.matFromSystem(system)
        val routeFlow: Flow<HttpRequest, HttpResponse, NotUsed> = route.flow(classicSystem, materializer)
        val futureBinding: CompletionStage<ServerBinding> =
            http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer)
        futureBinding.whenComplete { binding: ServerBinding?, exception: Throwable? ->
            if (binding != null) {
                val address: InetSocketAddress = binding.localAddress()
                logger.info { "Server online at http://${address.hostString}:${address.port}" }
            } else {
                logger.error { "Failed to bind HTTP endpoint, terminating system : $exception" }
                system.terminate()
            }
        }
    }

}

fun main(args: Array<String>) {
    val rootBehavior =
        Behaviors.setup { context: ActorContext<NotUsed> ->
            val profileRepository = ProfileRepository()
            val metaInfoMaker = context.spawn(MetaInfoMaker.create(), "meta-info")
            val histogramMaker = context.spawn(HistogramMaker.create(), "histogram")
            val statisticsMaker = context.spawn(StatisticsMaker.create(), "statistics")
            val userRoutes = ProfileRoute(
                context.system,
                profileRepository,
                metaInfoMaker,
                histogramMaker,
                statisticsMaker
            )
            ImageProfilerApplication().run(userRoutes.createRoute(), context.system)
            Behaviors.empty()
        }

    ActorSystem.create(rootBehavior, "image-profile-server")
}
