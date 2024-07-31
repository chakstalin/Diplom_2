package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import api.pojo.UserPOJO;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    public UserClient() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Регистрация пользователя")
    public static Response registerUser(UserPOJO user) {
        return given()
                .contentType("application/json")
                .body(user)
                .post(REGISTER_PATH);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(UserPOJO user) {
        return given()
                .contentType("application/json")
                .body(user)
                .post(LOGIN_PATH);
    }

    @Step("Удаление пользователя с токеном")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .delete(USER_PATH);
    }

    @Step("Обновление данных пользователя с токеном")
    public Response updateUser(UserPOJO user, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(user)
                .patch(USER_PATH);
    }

    @Step("Обновление данных пользователя без токена")
    public Response updateUserWithOutToken(UserPOJO user) {
        return given()
                .contentType("application/json")
                .body(user)
                .patch(USER_PATH);
    }
}
