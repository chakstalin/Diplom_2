import api.client.IngredientClient;
import api.client.OrderClient;
import api.pojo.OrderPOJO;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private String accessToken;
    private List<String> ingredientIds;
    private List<String> bunIds;
    private List<String> sauceIds;
    private List<String> fillingIds;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        Response response = userClient.registerUser(user);
        accessToken = response.jsonPath().getString("accessToken");

        // Получение списков ингредиентов
        ingredientIds = ingredientClient.getAllIngredientsIds();
        bunIds = ingredientClient.getAllBunsIds();
        sauceIds = ingredientClient.getAllSaucesIds();
        fillingIds = ingredientClient.getAllFillingsIds();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void testCreateOrderWithAuthAndIngredientsShouldBeSuccessful() {
        OrderPOJO order = new OrderPOJO(ingredientIds);
        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuthShouldReturnError() {
        OrderPOJO order = new OrderPOJO(ingredientIds);

        Response response = orderClient.createOrderWithoutAuth(order);

        response.then().statusCode(401)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredientsShouldReturnError() {
        OrderPOJO order = new OrderPOJO(List.of());

        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredientHashShouldReturnError() {
        OrderPOJO order = new OrderPOJO(List.of("invalidIngredient"));

        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(500);
    }

    @Test
    @DisplayName("Проверка цены заказа")
    public void testOrderPriceCalculationShouldBeCorrect() {
        OrderPOJO order = new OrderPOJO(ingredientIds);
        int expectedPrice = IngredientClient.calculateOrderPrice(ingredientIds);

        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());

        int actualPrice = orderClient.getOrderPrice(response);
        assertThat("Цена заказа " + actualPrice + " не соответствует ожидаемой " + expectedPrice, actualPrice == expectedPrice);
    }

    @Test
    @DisplayName("Создание заказа с одним ингредиентом булочки")
    public void testCreateOrderWithOneBunShouldBeSuccessful() {
        OrderPOJO order = new OrderPOJO(List.of(bunIds.get(0)));

        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Невозможность создать заказ только из соусов")
    public void testCreateOrderWithOnlySaucesShouldReturnError() {
        OrderPOJO order = new OrderPOJO(sauceIds);

        Response response = orderClient.createOrder(order, accessToken);
        System.out.println(sauceIds);
        response.then().statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Невозможность создать заказ только из начинок")
    public void testCreateOrderWithOnlyFillingsShouldReturnError() {
        OrderPOJO order = new OrderPOJO(fillingIds);

        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(400)
                .body("success", equalTo(false));
    }

}
