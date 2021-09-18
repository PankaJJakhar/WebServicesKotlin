package com.pixcel.plugins

import com.pixcel.routes.customerRouting
import com.pixcel.routes.registerCustomerRoutes
import com.pixcel.routes.registerOrderRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    install(AutoHeadResponse)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        registerCustomerRoutes()
        registerOrderRoutes()
    }
}
