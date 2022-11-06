package routes

import dao.daoFacade as dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.articleRouting() {
    route("/articles") {
        get {
            if (dao.allArticles().isEmpty()) {
                call.respondText("No articles found",
                    status=HttpStatusCode.OK)
            } else {
                call.respond(dao.allArticles())
            }
        }
    }
}
