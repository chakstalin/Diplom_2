import api.client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTest extends BaseTest{
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        Response response = userClient.registerUser(user);
        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    public void testGetUserOrdersWithAuthShouldBeSuccessful() {
        Response response = orderClient.getUserOrders(accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованным пользователем")
    public void testGetUserOrdersWithoutAuthShouldReturnError() {
        Response response = orderClient.getUserOrdersWithoutAuth();

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}

