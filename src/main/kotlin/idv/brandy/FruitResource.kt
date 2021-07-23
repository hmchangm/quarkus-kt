package idv.brandy

import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FruitResource {

    @Inject
    lateinit var fruitRepository: FruitRepository

    @GET
    fun list():MutableList<Fruit>{
        return fruitRepository.findAll().list()
    }

    @POST
    @Transactional
    fun add(fruit: Fruit): MutableList<Fruit> {
        fruitRepository.persist(fruit)
        return fruitRepository.findAll().list()
    }

    @PUT
    @Path("/{uuid}")
    @Transactional
    fun modify(@PathParam("uuid") uuid:String, fruit: Fruit): MutableList<Fruit> {
        val myFruit = fruitRepository.findById(uuid)
        myFruit.name= fruit.name
        fruitRepository.persist(myFruit)
        return fruitRepository.findAll().list()
    }

    @DELETE
    @Path("/{uuid}")
    @Transactional
    fun delete(@PathParam("uuid") uuid:String):  MutableList<Fruit> {
        val myFruit = fruitRepository.findById(uuid)
        fruitRepository.delete(myFruit)
        return fruitRepository.findAll().list()
    }

}