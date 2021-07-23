package idv.brandy

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType

@QuarkusTest
class FruitResourceTest {
    @Test
    fun testList() {
        RestAssured.given()
                .`when`()["/fruits"]
                .then()
                .statusCode(200)
                .body("$.size()", CoreMatchers.`is`(2),
                        "name", Matchers.containsInAnyOrder("Apple", "Pineapple"),
                        "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit"))
    }

    @Test
    fun testAdd() {
        RestAssured.given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .`when`()
                .post("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", CoreMatchers.`is`(3),
                        "name", Matchers.containsInAnyOrder("Apple", "Pineapple", "Pear"),
                        "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit", "Winter fruit"))
        RestAssured.given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .`when`()
                .delete("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", CoreMatchers.`is`(2),
                        "name", Matchers.containsInAnyOrder("Apple", "Pineapple"),
                        "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit"))
    }
}