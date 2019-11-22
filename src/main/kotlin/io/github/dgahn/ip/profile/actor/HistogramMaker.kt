package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.util.ImageUtil


class HistogramMaker {

    class MakeHistogram(val replyTo: ActorRef<Histogram>)

    companion object {

        fun create(registerDto: ProfileRegisterDto): Behavior<MakeHistogram> {
            return Behaviors.receiveMessage { makeHistogram ->
                onMakeHistogram(registerDto, makeHistogram)
            }
        }

        private fun onMakeHistogram(
            registerDto: ProfileRegisterDto,
            message: MakeHistogram
        ): Behavior<MakeHistogram?> {
            message.replyTo.tell(Histogram(ImageUtil.getImageStatistics(registerDto.file)!!.getHistogram()))
            return Behaviors.same()
        }

    }

}
