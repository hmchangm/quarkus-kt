package idv.brandy

import arrow.core.None
import arrow.core.Some
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/v2/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FruitRestFp {
    @Inject
    lateinit var fruitRepository: FruitRepository

    @GET
    suspend fun list(): MutableList<Fruit> = fruitRepository.findAll().list()

    @POST
    @Transactional
    suspend fun add(fruit: Fruit): Response {
        fruitRepository.persist(fruit);
        return Response.status(Response.Status.CREATED).entity(fruit).build()
    }

    @PUT
    @Path("/{uuid}")
    @Transactional
    suspend fun modify(@PathParam("uuid") uuid: String, fruit: Fruit): Response {
        return when (val fruitOption = fruitRepository.findByUuid(uuid)) {
            is None -> noThisFruitResponse()
            is Some -> return fruitOption.copy().value.let { it.name = fruit.name; it }
                .let { fruitRepository.persist(it); Response.ok(it).build() }
        }
    }

    @GET
    @Path("/{uuid}")
    suspend fun getFruit(@PathParam("uuid") uuid: String): Response {
        return when (val fruitOption = fruitRepository.findByUuid(uuid)) {
            is None -> noThisFruitResponse()
            is Some -> return fruitOption.value.let { Response.ok(it).build() }
        }
    }

    @DELETE
    @Path("/{uuid}")
    @Transactional
    suspend fun delete(@PathParam("uuid") uuid: String): Response {
        return when (val fruitOption = fruitRepository.findByUuid(uuid)) {
            is None -> noThisFruitResponse()
            is Some -> return fruitOption.value.let {
                fruitRepository.delete(it)
            }.let { Response.noContent().build() }
        }
    }

    private fun noThisFruitResponse() = Response.serverError().entity("No this fruit").build()
}





