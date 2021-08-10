package idv.brandy

import arrow.core.Either
import idv.brandy.model.Fruit
import idv.brandy.service.FruitService
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/v2/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FruitRestFp(val fruitService: FruitService) {

    @GET
    fun list(): Response = when (val e = fruitService.findAll()) {
        is Either.Left -> throw RuntimeException("data store access error ${e.value}")
        is Either.Right -> Response.ok(e.value).build()
    }

    @POST
    @Transactional
    fun add(fruit: Fruit): Response = when (val e = fruitService.save(fruit)) {
        is Either.Left -> throw RuntimeException("data store access error ${e.value}")
        is Either.Right -> Response.ok(fruit).status(201).build()
    }

    @PUT
    @Path("/{uuid}")
    @Transactional
    fun modify(@PathParam("uuid") uuid: String, fruit: Fruit): Response =
        when (val e = fruitService.modify(uuid, fruit)) {
            is Either.Left -> Response.serverError().entity("data store access error ${e.value}").build()
            is Either.Right -> Response.ok(e.value).build()
        }


    @GET
    @Path("/{uuid}")
    fun getFruit(@PathParam("uuid") uuid: String): Response =
        when (val e = fruitService.findByUuid(uuid)) {
            is Either.Left -> Response.serverError().entity("exception ${e.value}").build()
            is Either.Right -> Response.ok(e.value).build()
        }

    @DELETE
    @Path("/{uuid}")
    @Transactional
    fun delete(@PathParam("uuid") uuid: String): Response =
        when (val e = fruitService.deleteByUuid(uuid)) {
            is Either.Left -> Response.serverError().entity("exception ${e.value}").build()
            is Either.Right -> Response.status(204).build()
        }
}





