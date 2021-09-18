package com.pixcel.routes

import com.pixcel.models.Order
import com.pixcel.plugins.orderStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.registerOrderRoutes() {
    routing {
        listOrdersRoute()
        getOrderRoute()
        postOrderRoute()
        totalizeOrderRoute()
    }
}


fun Route.listOrdersRoute() {
    get("/order") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        }
    }
}

fun Route.getOrderRoute() {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )

        call.respond(order)
    }
}

fun Route.postOrderRoute() {
    post("/order") {
        val order = call.receive<Order>()

        val orderFound = orderStorage.find { it.number == order.number }

        if (orderFound != null) {
            call.respond(HttpStatusCode.Conflict, "Order already exists")
        } else {
            orderStorage.add(order)
            call.respondText("Order stored correctly", status = HttpStatusCode.Created)
        }
    }
}

fun Route.totalizeOrderRoute() {
    get("/order/{id}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )

        val total = order.contents.map { it.price * it.amount }.sum()

        call.respond(total)
    }
}

