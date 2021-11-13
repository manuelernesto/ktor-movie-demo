package io.github.manuelernesto.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

/**
 * @author  Manuel Ernesto (manuelernest0)
 * @date  13/11/21 14:15
 * @version 1.0
 */

//model
@Serializable
data class Movie(val id: Int, val title: String, val year: Int, val genre: String)

//database
val db = mutableListOf<Movie>()

fun Route.movies(){
    route("/api/movies"){
        get {
            if(db.isEmpty()){
                call.respondText ("Lista vazia", status = HttpStatusCode.NoContent)
            }
            else{
                call.respond(db)
            }
        }

        post {
            val movie = call.receive<Movie>()
            db.add(movie)
            call.respondText("Filme adicionado com sucesso", status = HttpStatusCode.Created)
        }

        get("{id}"){
            val id =call.parameters["id"]?: return@get call.respond(HttpStatusCode.BadRequest)


            val movie =db.find{ it.id == id.toInt()} ?: call.respondText(
                "No movie with id:$id",
                status = HttpStatusCode.NotFound
            )

            call.respond(movie)
        }

        delete("/{id}") {
           val id =  call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if(db.removeIf { it.id == id.toInt() }){
                call.respondText("Filme removido com sucesso", status = HttpStatusCode.OK)
            }else{
                call.respondText("Filme não encontrado", status = HttpStatusCode.NotFound)
            }
        }

    }
}