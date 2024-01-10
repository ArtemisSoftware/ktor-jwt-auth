package com.artemissoftware.plugins

import com.artemissoftware.data.models.user.UserDataSource
import com.artemissoftware.routes.auth.authenticate
import com.artemissoftware.routes.auth.getSecretInfo
import com.artemissoftware.routes.auth.signIn
import com.artemissoftware.routes.auth.signUp
import com.artemissoftware.security.hashing.HashingService
import com.artemissoftware.security.token.TokenConfig
import com.artemissoftware.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
