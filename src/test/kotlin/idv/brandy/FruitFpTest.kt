package idv.brandy

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.locationtech.jts.util.Assert
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
            .body("""{"name": "Pear", "description": "Winter fruit"}""")
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
    fun testModify() {
        val response = RestAssured.given()
            .body("""{"id":"fasgrgwrarffdff","name": "AnotherName", "description": "DF"}""")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()
            .put("/v2/fruits/fasgrgwrarffdff")
            .then()
            .statusCode(200)
            .body(
                containsString("AnotherName")
            )

        RestAssured.given()
            .body("""{"id":"fasgrgwrarffdff","name": "Pineapple", "description": "DFS"}""")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`()
            .put("/v2/fruits/fasgrgwrarffdff")
            .then()
            .statusCode(200).body(
                containsString("Pineapple")
            )


    }


    @Test
    fun testGet() {
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

    @Test
    fun testDeleteError() {
        val response = RestAssured.given().header("Content-Type", MediaType.APPLICATION_JSON)
            .`when`().delete("/v2/fruits/BBBBDDASDAS")
            .then().statusCode(500).extract().response().body.print()
        Assert.isTrue(response.contains("NoThisFruit"))
    }
}