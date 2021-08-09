package idv.brandy

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
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
    fun list(): List<Fruit> = when (val either = fruitService.findAll()) {
        is Either.Left -> throw RuntimeException("data store access error", either.value)
        is Either.Right -> either.value
    }

    @POST
    @Transactional
    fun add(fruit: Fruit): Response = when (val either = fruitService.save(fruit)) {
        is Either.Left -> throw RuntimeException("data store access error", either.value)
        is Either.Right -> Response.ok(fruit).status(201).build()
    }

    @PUT
    @Path("/{uuid}")
    @Transactional
    fun modify(@PathParam("uuid") uuid: String, fruit: Fruit): Fruit =
        when (val either = fruitService.findByUuid(uuid)) {
            is Either.Left -> throw RuntimeException("data store access error", either.value)
            is Either.Right -> when (val opt = either.value) {
                is None -> throw RuntimeException("no this fruit $uuid")
                is Some -> opt.value.copy(name = fruit.name).let { fruitService.save(it);it }
            }
        }


    @GET
    @Path("/{uuid}")
    fun getFruit(@PathParam("uuid") uuid: String): Fruit =
        when (val either = fruitService.findByUuid(uuid)) {
            is Either.Left -> throw RuntimeException("data store access error", either.value)
            is Either.Right -> when (val opt = either.value) {
                is None -> throw RuntimeException("no this fruit $uuid")
                is Some -> opt.value
            }
        }

    @DELETE
    @Path("/{uuid}")
    @Transactional
    fun delete(@PathParam("uuid") uuid: String): Response =
        when (val either = fruitService.findByUuid(uuid)) {
            is Either.Left -> throw RuntimeException("data store access error", either.value)
            is Either.Right -> when (val opt = either.value) {
                is None -> throw RuntimeException("no this fruit $uuid")
                is Some -> {
                    opt.value.let { fruitService.delete(it) }
                    Response.status(204).build()
                }
            }
        }

}





