package com.artemissoftware

import com.artemissoftware.data.models.user.MongoUserDataSource
import com.artemissoftware.data.models.user.User
import com.artemissoftware.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://artemis:$mongoPw@cluster0.n2mogbs.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)

    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
