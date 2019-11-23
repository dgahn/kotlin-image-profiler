package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.actor.HistogramMaker.HistogramCommand
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.util.ImageUtil


class HistogramMaker(context: ActorContext<HistogramCommand>) : AbstractBehavior<HistogramCommand>(context) {

    interface HistogramCommand

    class HistogramCompleted(val message: Histogram) : HistogramCommand

    class MakeHistogram(val registerDto: ProfileRegisterDto, val replyTo: ActorRef<HistogramCompleted>) : HistogramCommand

    companion object {

        @JvmStatic
        fun create(): Behavior<HistogramCommand> {
            return Behaviors.setup { context: ActorContext<HistogramCommand> ->
                HistogramMaker(context)
            }
        }

    }

    private fun onMakeHistogram(message: MakeHistogram): Behavior<HistogramCommand> {
        message.replyTo.tell(
            HistogramCompleted(Histogram(ImageUtil.getImageStatistics(message.registerDto.file)!!.getHistogram()))
        )
        return Behaviors.same()
    }

    override fun createReceive(): Receive<HistogramCommand> {
        return newReceiveBuilder()
            .onMessage(
                MakeHistogram::class.java
            ) { command: MakeHistogram -> onMakeHistogram(command) }
            .build()
    }

}
