import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import api.client.UserClient;
import api.pojo.UserPOJO;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void testRegisterUserUniqueUserShouldBeCreated() {
        Response response = userClient.registerUser(user);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void testRegisterUserExistingUserShouldReturnError() {
        userClient.registerUser(user);

        // Пытаемся создать того же пользователя
        Response response = userClient.registerUser(user);

        response.then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

        // Осталяю попытку взять токен для удаления данных, если тест провалится
        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    public void testRegisterUserMissingFieldShouldReturnError() {
        user = new UserPOJO(password, name);
        Response response = userClient.registerUser(user);

        response.then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

        // Осталяю попытку взять токен для удаления данных, если тест провалится
        accessToken = response.jsonPath().getString("accessToken");
    }
}
