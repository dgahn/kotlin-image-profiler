package io.github.dgahn.ip

import mu.KotlinLogging


class ImageProfilerApplication {

    private val logger = KotlinLogging.logger {}

    fun run() {
        logger.debug { "Application Running" }
    }

}

fun main(args: Array<String>) {
    ImageProfilerApplication().run()
}
