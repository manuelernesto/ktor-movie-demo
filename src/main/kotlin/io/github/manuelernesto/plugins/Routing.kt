package io.github.manuelernesto.plugins

import io.github.manuelernesto.routes.moviesRoute
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        moviesRoute()
    }
}
