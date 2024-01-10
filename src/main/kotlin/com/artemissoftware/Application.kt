package com.artemissoftware

import com.artemissoftware.data.models.user.MongoUserDataSource
import com.artemissoftware.plugins.*
import com.artemissoftware.security.hashing.SHA256HashingService
import com.artemissoftware.security.token.JwtTokenService
import com.artemissoftware.security.token.TokenConfig
import io.ktor.server.application.*
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

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig = tokenConfig)
    configureRouting(hashingService = hashingService, userDataSource = userDataSource, tokenService = tokenService, tokenConfig = tokenConfig)
}
