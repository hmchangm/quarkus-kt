package idv.brandy

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType

@QuarkusTest
class FruitFpTest {
    @Test
    fun testList() {
        RestAssured.given().header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()["/v2/fruits"]
            .then()
            .statusCode(200)
            .body(
                "$.size()", CoreMatchers.`is`(2),
                "name", Matchers.containsInAnyOrder("Apple", "Pineapple"),
                "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit")
            )
    }

    @Test
    fun testAdd() {
        val response = RestAssured.given()
            .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()
            .post("/v2/fruits")
            .then()
            .statusCode(201).extract().response()
        val uuid = response.jsonPath().getString("uuid")

        RestAssured.given().header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`().delete("/v2/fruits/$uuid")
            .then().statusCode(204)


    }
    @Test
    fun testGet(){
        RestAssured.given()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()["/v2/fruits/AAAFBBD"]
            .then()
            .statusCode(500).extract().response()
        RestAssured.given()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()["/v2/fruits/fasgrgwrarffdff"]
            .then()
            .statusCode(200).extract().response()
    }
}