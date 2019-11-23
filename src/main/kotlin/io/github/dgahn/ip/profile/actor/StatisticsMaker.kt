package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.actor.StatisticsMaker.StatisticsCommand
import io.github.dgahn.ip.profile.embedded.Statistics
import java.io.File
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
        message.replyTo.tell(StatisticsCompleted(createStatistics(message.registerDto.file)))
        return Behaviors.same()
    }

    override fun createReceive(): Receive<StatisticsCommand> {
        return newReceiveBuilder()
            .onMessage(MakeStatistics::class.java) { command: MakeStatistics -> onMakeStatistics(command) }
            .build()
    }

    private fun createStatistics(file: File): Statistics {
        val bufferedImage = ImageIO.read(file)

        val width = bufferedImage.width
        val height = bufferedImage.height

        val rgbSum = RgbSum(width, height)
        val statistics = Statistics()
        for (x in 0 until width) {
            for (y in 0 until height) {
                val rgb = Rgb(bufferedImage.getRGB(x, y))

                setMaxAndMin(statistics, rgb)
                setSum(rgbSum, rgb)
            }
        }

        setAvg(statistics, rgbSum)

        return statistics
    }

    private fun setMaxAndMin(statistics: Statistics,
                             rgb: Rgb) {
        if (statistics.rMax < rgb.r) {
            statistics.rMax = rgb.r
        }

        if (statistics.rMin > rgb.r) {
            statistics.rMin = rgb.r
        }

        if (statistics.gMax < rgb.g) {
            statistics.gMax = rgb.g
        }

        if (statistics.gMin > rgb.g) {
            statistics.gMin = rgb.g
        }

        if (statistics.bMax < rgb.b) {
            statistics.bMax = rgb.b
        }

        if (statistics.bMin > rgb.b) {
            statistics.bMin = rgb.b
        }
    }

    private fun setSum(rgbSum: RgbSum, rgb: Rgb) {
        rgbSum.rSum += rgb.r
        rgbSum.gSum += rgb.g
        rgbSum.bSum += rgb.b
    }

    private fun setAvg(statistics: Statistics, rgb: RgbSum) {
        val pixelCount: Double = (rgb.width * rgb.height).toDouble()
        statistics.rAvg = kotlin.math.floor(rgb.rSum / pixelCount)
        statistics.bAvg = kotlin.math.floor(rgb.bSum / pixelCount)
        statistics.gAvg = kotlin.math.floor(rgb.gSum / pixelCount)
    }

    class Rgb(rgb: Int) {
        var r: Int = rgb.shr(16) and 0xFF
        var g: Int = rgb.shr(8) and 0xFF
        var b: Int = rgb and 0xFF
    }

    class RgbSum(val width: Int, val height: Int) {
        var rSum = 0
        var gSum = 0
        var bSum = 0
    }

}
