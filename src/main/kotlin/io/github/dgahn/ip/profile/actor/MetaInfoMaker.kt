package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.actor.MetaInfoMaker.MetaInfoCommand
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.util.ImageUtil


class MetaInfoMaker(context: ActorContext<MetaInfoCommand>) : AbstractBehavior<MetaInfoCommand>(context) {

    interface MetaInfoCommand

    class MetaInfoCompleted(val message: MetaInfo) : MetaInfoCommand

    class MakeMetaInfo(val registerDto: ProfileRegisterDto, val replyTo: ActorRef<MetaInfoCompleted>) : MetaInfoCommand

    companion object {

        @JvmStatic
        fun create(): Behavior<MetaInfoCommand> {
            return Behaviors.setup { context: ActorContext<MetaInfoCommand> ->
                MetaInfoMaker(context)
            }
        }

    }

    private fun onMakeMetaInfo(message: MakeMetaInfo): Behavior<MetaInfoCommand> {
        message.replyTo.tell(
            MetaInfoCompleted(
                createMetaInfo(
                    message.registerDto.name,
                    ImageUtil.getMetadataMap(message.registerDto.file)!!
                )
            )
        )
        return Behaviors.same()
    }

    private fun createMetaInfo(name: String, map: Map<String, String>): MetaInfo {
        return MetaInfo(
            name,
            (map["Image Width"] ?: error("0")).replace("[^0-9]".toRegex(), "").toInt(),
            (map["Image Height"] ?: error("0")).replace("[^0-9]".toRegex(), "").toInt(),
            map["File Modified Date"] ?: error("error")
        )
    }

    override fun createReceive(): Receive<MetaInfoCommand> {
        return newReceiveBuilder()
            .onMessage(MakeMetaInfo::class.java) { command: MakeMetaInfo -> onMakeMetaInfo(command) }
            .build()
    }

}
