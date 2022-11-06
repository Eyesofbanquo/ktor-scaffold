package dao

import kotlinx.coroutines.Dispatchers
import models.Articles
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

// https://towardsdev.com/set-up-postgres-database-with-kotlin-ktor-fba5ea00f7e6
object DatabaseFactory {
    private val host = System.getenv("DB_HOST") ?: "localhost"
    private val port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432
    private val dbName = System.getenv("DB_NAME") ?: "ktor_example"
    private val dbUser = System.getenv("DB_USER") ?: "markimshaw"
    private val dbPassword = System.getenv("DB_PASSWORD") ?: "aE20400112!"
    fun init() {
        var envUrl = System.getenv("DATABASE_URL")
        var database: Database = if (envUrl.isNullOrEmpty()) {
            Database.connect(
                url = "jdbc:postgresql://$host:$port/$dbName",
                driver = "org.postgresql.Driver",
                user = dbUser,
                password = dbPassword,
            )
        } else {
            val uri = URI(envUrl)
            val username = uri.userInfo.split(":").toTypedArray()[0]
            val password = uri.userInfo.split(":").toTypedArray()[1]
            Database.connect(
                url = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}",
                driver = "org.postgresql.Driver",
                user = username,
                password = password
            )
        }
//
//        val database = Database.connect(
//            url = url,
//            driver = "org.postgresql.Driver",
//            user = dbUser,
//            password = dbPassword,
//        )
        transaction(database) {
            SchemaUtils.create(Articles)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}