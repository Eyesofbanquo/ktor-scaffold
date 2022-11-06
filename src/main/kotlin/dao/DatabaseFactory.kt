package dao

import kotlinx.coroutines.Dispatchers
import models.Articles
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    private val host = System.getenv("DB_HOST") ?: "localhost"
    private val port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432
    private val dbName = System.getenv("DB_NAME") ?: "ktor_example"
    private val dbUser = System.getenv("DB_USER") ?: "markimshaw"
    private val dbPassword = System.getenv("DB_PASSWORD") ?: "aE20400112!"
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(
            url = "jdbc:postgresql://$host:$port/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword,
        )
        transaction(database) {
            SchemaUtils.create(Articles)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}