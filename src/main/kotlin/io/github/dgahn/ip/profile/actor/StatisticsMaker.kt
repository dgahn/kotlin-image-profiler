package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import ij.process.ImageStatistics
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.embedded.Statistics
import io.github.dgahn.ip.util.ImageUtil


class StatisticsMaker {

    class MakeStatistics(val replyTo: ActorRef<Statistics>)

    companion object {

        fun create(registerDto: ProfileRegisterDto): Behavior<MakeStatistics> {
            return Behaviors.receiveMessage { makeStatistics ->
                onMakeStatistics(registerDto, makeStatistics)
            }
        }

        private fun onMakeStatistics(
            registerDto: ProfileRegisterDto,
            message: MakeStatistics
        ): Behavior<MakeStatistics?> {
            message.replyTo.tell(createStatistics(ImageUtil.getImageStatistics(registerDto.file)!!))
            return Behaviors.same()
        }

        private fun createStatistics(statistics: ImageStatistics): Statistics {
            val max = statistics.max
            val min = statistics.min
            val avg = (max + min) / 2
            return Statistics(max, min, avg)
        }

    }

}
