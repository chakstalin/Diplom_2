import api.client.UserClient;
import api.datagenerator.UserDataGenerator;
import api.pojo.UserPOJO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected String baseUrl = "https://stellarburgers.nomoreparties.site";
    protected String accessToken;
    protected String email;
    protected String password;
    protected String name;
    protected UserPOJO user;
    protected UserClient userClient;

    @Before
    public void setUpBase() {
        userClient = new UserClient();
        RestAssured.baseURI = baseUrl;
        email = UserDataGenerator.generateRandomEmail();
        password = UserDataGenerator.generateRandomPassword();
        name = UserDataGenerator.generateRandomName();
        user = new UserPOJO(email, password, name);
    }

    @After
    public void tearDownBase() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }

}
