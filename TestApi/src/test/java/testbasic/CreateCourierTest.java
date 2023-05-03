package testbasic;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.manage.CourierManager;
import org.pojo.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateCourierTest  {
    private final String login = "Violetta";
    private final String password = "1234";
    private final String firstName = "saske";
    private Courier courier;
    private CourierManager courierManager;
    public Response createCourierFirst;


    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";

        courierManager = new CourierManager();
        courier = new Courier(login, password,firstName);
        createCourierFirst= courierManager.createCourier(courier);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка: курьера можно создать, успешный запрос возвращает ok: true,запрос возвращает правильный код ответа;")
    public void createCourierFirstStatus200()
    {
        createCourierFirst.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка нельзя создать одинковых курьеров; если создать пользователя с логином, который уже есть,\n" +
            "возвращается ошибка. ")
    public void createCourierSecondStatus200() {
        courierManager.createCourier(courier)
                .then()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Создание курьера без одного поля")
    @Description("если одного из полей нет, запрос возвращает ошибку;чтобы создать курьера, нужно передать в ручку все обязательные поля;")
    public void createCourierWithoutOneField() {
        courier = new Courier(null, password, firstName);
        courierManager.createCourier(courier)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @After
    public void tearDown()
    {
        courier = new Courier(login,password);
        courierManager.deleteCourier(courierManager.idCourier(courier));
    }
}
