package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import api.pojo.OrderPOJO;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_PATH = "/api/orders";

    public OrderClient() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Создание заказа")
    public Response createOrder(OrderPOJO order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(order)
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(OrderPOJO order) {
        return given()
                .contentType("application/json")
                .body(order)
                .post(ORDERS_PATH);
    }

    @Step("Получение заказов пользователя с токеном: {accessToken}")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(ORDERS_PATH);
    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .get(ORDERS_PATH);
    }

    @Step("Получение полной стоимости заказа")
    public int getOrderPrice(Response getOrderResponse) {
        return getOrderResponse.jsonPath().getInt("order.price");
    }

}
