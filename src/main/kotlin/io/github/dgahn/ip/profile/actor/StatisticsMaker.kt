package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import ij.process.ImageStatistics
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCommand
import io.github.dgahn.ip.profile.embedded.Statistics
import io.github.dgahn.ip.util.ImageUtil
import java.io.File
import java.lang.Math.floor
import javax.imageio.ImageIO


class StatisticsMaker(context: ActorContext<StatisticsCommand>) : AbstractBehavior<StatisticsCommand>(context) {

    interface StatisticsCommand

    class StatisticsCompleted(val message: Statistics) : StatisticsCommand

    class MakeStatistics(val registerDto: ProfileRegisterDto, val replyTo: ActorRef<StatisticsCompleted>) :
        StatisticsCommand

    companion object {

        @JvmStatic
        fun create(): Behavior<StatisticsCommand> {
            return Behaviors.setup { context: ActorContext<StatisticsCommand> ->
                StatisticsMaker(context)
            }
        }

    }

    private fun onMakeStatistics(message: MakeStatistics): Behavior<StatisticsCommand> {
        message.replyTo.tell(StatisticsCompleted(ImageUtil.getStatistics(message.registerDto.file)))
        return Behaviors.same()
    }

    override fun createReceive(): Receive<StatisticsCommand> {
        return newReceiveBuilder()
            .onMessage(MakeStatistics::class.java) { command: MakeStatistics -> onMakeStatistics(command) }
            .build()
    }

}
