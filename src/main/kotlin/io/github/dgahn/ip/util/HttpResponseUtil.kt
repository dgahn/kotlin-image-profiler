package io.github.dgahn.ip.util

import akka.http.javadsl.model.ContentTypes
import akka.http.javadsl.model.HttpEntities
import akka.http.javadsl.model.HttpEntity
import akka.http.javadsl.model.HttpResponse
import akka.http.javadsl.model.StatusCodes
import akka.http.javadsl.server.Directives
import akka.http.javadsl.server.ExceptionHandler
import akka.http.javadsl.server.Route
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.dgahn.ip.profile.exception.FileReadFailException
import io.github.dgahn.ip.profile.exception.ProfileNotFoundException
import io.github.dgahn.ip.profile.exception.ProfileSaveFailException

class HttpResponseUtil {

    companion object {
        private val objectMapper: ObjectMapper = ObjectMapper()

        fun <T> responseOK(t: T): Route {
            val entity: HttpEntity.Strict? =
                HttpEntities.create(ContentTypes.APPLICATION_JSON, objectMapper.writeValueAsString(t))
            val response = HttpResponse.create()
                .withStatus(StatusCodes.OK)
                .withEntity(entity)
            return Directives.complete(response)
        }

        fun responseCreated(): Route {
            val entity
                    = HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8, "Resource Created.")
            val response = HttpResponse.create()
                .withStatus(StatusCodes.CREATED)
                .withEntity(entity)
            return Directives.complete(response)
        }

        fun exceptionHandler(): ExceptionHandler {
            return ExceptionHandler.newBuilder()
                .match(ProfileNotFoundException::class.java) { Directives.complete(StatusCodes.NO_CONTENT) }
                .match(FileReadFailException::class.java) { Directives.complete(StatusCodes.BAD_REQUEST, it.message) }
                .match(ProfileSaveFailException::class.java) { Directives.complete(StatusCodes.INTERNAL_SERVER_ERROR, it.message) }
                .build()
        }

    }
}
