package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    public IngredientClient() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Получение списка id ингредиентов")
    public List<String> getAllIngredientsIds() {
        Response response = given()
                .get(INGREDIENTS_PATH);

        response.then().statusCode(200);

        return response.jsonPath().getList("data._id", String.class);
    }

    @Step("Получение списка id булочек")
    public static List<String> getAllBunsIds() {
        Response response = given()
                .get(INGREDIENTS_PATH);

        response.then().statusCode(200);

        return response.jsonPath().getList("data.findAll { it.type == 'bun' }._id", String.class);
    }

    @Step("Получение списка id соусов")
    public List<String> getAllSaucesIds() {
        Response response = given()
                .get(INGREDIENTS_PATH);

        response.then().statusCode(200);

        return response.jsonPath().getList("data.findAll { it.type == 'sauce' }._id", String.class);
    }

    @Step("Получение списка id начинок")
    public List<String> getAllFillingsIds() {
        Response response = given()
                .get(INGREDIENTS_PATH);

        response.then().statusCode(200);

        return response.jsonPath().getList("data.findAll { it.type == 'filling' }._id", String.class);
    }

    @Step("Получение цены ингредиента по id")
    public static int getIngredientPrice(String id) {
        Response response = given()
                .get(INGREDIENTS_PATH);

        List<Integer> prices = response.jsonPath().getList("data.findAll { it._id == '" + id + "' }.price", Integer.class);

        if (prices.isEmpty()) {
            throw new IllegalArgumentException("Ингредиент не найден: " + id);
        }

        return prices.get(0);
    }

    @Step("Расчет общей цены заказа")
    public static int calculateOrderPrice(List<String> ingredientIds) {
        List<String> bunIds = getAllBunsIds();
        int totalPrice = 0;
        for (String id : ingredientIds) {
            if (bunIds.contains(id)) {
                // Учитываем, что цена булочки умножается на 2
                totalPrice += getIngredientPrice(id) * 2;
            } else {
                totalPrice += getIngredientPrice(id);
            }
        }
        return totalPrice;
    }
}

