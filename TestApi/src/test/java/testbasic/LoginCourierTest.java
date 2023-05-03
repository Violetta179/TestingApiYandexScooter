package testbasic;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import org.manage.CourierManager;
import org.pojo.Courier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
    private final String login = "Viola";
    private final String password = "1234";
    private final String firstName="Violetta";
    private final String loginError = "Error";
    private final String loginNoExist = "NoExist";
    private final String passwordError = "Error";
    private CourierManager courierManager;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";

        courierManager = new CourierManager();
        courier = new Courier(login,password,firstName);
        courierManager.createCourier(courier);
    }

    @Test
    @DisplayName("Авторизация курьера и получения статус кода 200, получения id")
    @Description("курьер может авторизоваться, для авторизации передать все поля")
    public void courierLogInIdStatus200()
    {
        courier = new Courier(login, password);
        courierManager.loginCourier(courier).then().assertThat().body("id", notNullValue()).and().statusCode(200);
    }

    @Test
    @DisplayName("Аторизация курьера без правильного логина")
    @Description("система вернёт ошибку, если неправильно указать логин;")
    public void courierLogInErrorLoginStatus400MessageError()
    {
        courier = new Courier(loginError, password);
        courierManager.loginCourier(courier).then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Авторизация курьера с неправильным паролем")
    @Description("система вернёт ошибку, если неправильно указать пароль;")
    public void courierLogInErrorPasswordStatus404MessageError()
    {
        courier = new Courier(login, passwordError);
        courierManager.loginCourier(courier).then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Попытка авторизировать курьера без логина")
    @Description("если какого-то поля нет, запрос возвращает ошибку;")
    public void courierLogInWithoutLoginStatus400MessageError(){
        courier = new Courier(null, password);
        courierManager.loginCourier(courier).then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Test
    @DisplayName("Авторизация курьера под несуществующим пользоватлем")
    @Description("если авторизоваться под несуществующим пользователем,\n" +
            "запрос возвращает ошибку;")
    public void courierLogInNotExistStatus404MessageError()
    {
        courier = new Courier(loginNoExist,passwordError);
        courierManager.loginCourier(courier).then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @After
    public void tearDown()
    {
        courier = new Courier(login,password);
        courierManager.deleteCourier(courierManager.idCourier(courier));
    }
}
