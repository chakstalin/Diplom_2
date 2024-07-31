import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import api.client.UserClient;
import api.pojo.UserPOJO;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends BaseTest {

private UserPOJO userLogin;

    @Before
    public void setUp() {
        userClient.registerUser(user);
        userLogin = new UserPOJO(user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void testLoginUserValidCredentialsShouldBeSuccessful() {

        Response response = userClient.loginUser(userLogin);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Логин с неверной почтой")
    public void testLoginUserInvalidEmailShouldReturnError() {
        userLogin.setEmail("totalywronemail123123123123123123321@mail.com");

        Response response = userClient.loginUser(userLogin);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void testLoginUserInvalidPasswordShouldReturnError() {
        userLogin.setPassword("totalywronpassword123123123123123123321");

        Response response = userClient.loginUser(userLogin);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
