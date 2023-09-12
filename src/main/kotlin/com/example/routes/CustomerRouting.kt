package com.example.routes

import com.example.models.Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting()
{
    val customerList = mutableListOf<Customer>()
    route("/customer")
    {
        get{
            if(customerList.isEmpty())
            {
                call.respondText("Customers is Empty", status = HttpStatusCode.OK)
            }
            else
            {
                call.respond(customerList)
            }
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if(customerList.removeIf{it.id == id})
            {
                call.respondText("Customer Removed Successfully", status =  HttpStatusCode.Accepted)
            }
            else
            {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        get("{id?}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing Customer ID",
                status = HttpStatusCode.BadRequest
            )

            val customer = customerList.find { it.id == id } ?: return@get call.respondText(
                "Customer ID Not Found",
                status = HttpStatusCode.NotFound
            )

            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()
            customerList.add(customer)
            call.respondText("Customer Added Successfully", status = HttpStatusCode.OK)
        }
    }
}