package io.github.dgahn.ip.profile.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import io.github.dgahn.ip.profile.ProfileRegisterDto
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.util.ImageUtil


class MetaInfoMaker {

    class MakeMetaInfo(val replyTo: ActorRef<MetaInfo>)

    companion object {

        fun create(registerDto: ProfileRegisterDto): Behavior<MakeMetaInfo> {
            return Behaviors.receiveMessage { makeMetaInfo ->
                onMakeMetaInfo(registerDto, makeMetaInfo)
            }
        }

        private fun onMakeMetaInfo(
            registerDto: ProfileRegisterDto,
            message: MakeMetaInfo
        ): Behavior<MakeMetaInfo?> {
            message.replyTo.tell(createMetaInfo(registerDto.name, ImageUtil.getMetadataMap(registerDto.file)!!))
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

    }

}
