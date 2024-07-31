import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import api.client.UserClient;
import api.pojo.UserPOJO;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest extends BaseTest {

    @Before
    public void setUp() {
        // updatedUser = new UserPOJO(email, name);
        Response response = userClient.registerUser(user);

        // Получение accessToken
        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testUpdateUserNameWithAuthValidDataShouldBeUpdated() {
        String newName = "UpdatedUser";
        user.setName(newName);

        Response response = userClient.updateUser(user, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void testUpdateUserNameWithoutAuthShouldReturnError() {
        String newName = "UpdatedUser1";
        user.setName(newName);

        Response response = userClient.updateUserWithOutToken(user);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение почты пользователя")
    public void testUpdateUserEmailWithAuthShouldBeUpdated() {
        String newEmail = "updated" + user.getEmail();
        user.setEmail(newEmail);
        Response response = userClient.updateUser(user, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    public void testUpdateUserPasswordWithAuthShouldBeUpdated() {
        String newPassword = "UpdatedPassword";

        Response response = userClient.updateUser(user, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Изменение почты на существующую почту")
    public void testUpdateUserEmailToExistingEmailShouldReturnError() {
        String updatedEmail = "updated" + user.getEmail();
        user.setEmail(updatedEmail);
        userClient.registerUser(user);

        Response response = userClient.updateUser(user, accessToken);

        response.then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

}
