package io.github.manuelernesto.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

/**
 * @author  Manuel Ernesto (manuelernest0)
 * @date  04/12/21 10:18
 * @version 1.0
 */

//model
@Serializable
data class Movie(val id: Int, val title: String, val year: Int, val genre: String)

//database
val db = mutableListOf<Movie>()

fun Route.moviesRoute() {

    route("api/movies") {
        get {
            if (db.isEmpty())
                call.respondText("No movies were found in the database")
            else
                call.respond(db)
        }

        post {
            val movie = call.receive<Movie>()
            db.add(movie)
            call.respondText("Movie was successful added!", status = HttpStatusCode.Created)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val movie = db.find { it.id == id.toInt() } ?: call.respond(HttpStatusCode.NotFound)
            call.respond(movie)
        }



        put("{id}") {
            val movie = call.receive<Movie>()
            val index = db.indexOfFirst { it.id == movie.id }
            if (index != -1) {
                db[index] = movie
                call.respondText("Movie successfully updated", status = HttpStatusCode.OK)
            } else {
                call.respondText("Movie not found!", status = HttpStatusCode.NotFound)
            }
        }


        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (db.removeIf { it.id == id.toInt() }) {
                call.respondText("Movie deleted", status = HttpStatusCode.NoContent)
            } else {
                call.respondText("Movie not found!", status = HttpStatusCode.NotFound)
            }

        }

    }
}

