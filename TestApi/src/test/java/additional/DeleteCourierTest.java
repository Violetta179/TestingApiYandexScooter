package additional;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;


import org.pojo.Courier;
import org.manage.CourierManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class DeleteCourierTest {
    private final String login = "Violetta";
    private final String password = "1234";
    String firstName = "saske";
    private Courier courier;
    private CourierManager courierManager;
    String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";

        courier = new Courier(login, password,firstName);
        courierManager = new CourierManager();
        courierManager.createCourier(courier);
    }

    @Test
    @DisplayName("Удалить курьера и получить успешный ответ")
    @Description("успешный запрос возвращает ok: true;")
    public void deleteCourierOkTrue()
    {
        courier = new Courier(login, password);
        courierId = courierManager.idCourier(courier);
        courierManager.deleteCourier(courierId).then().assertThat().body("ok",equalTo(true)).statusCode(200);
    }

    @Test
    @DisplayName("Удаления курьера с несуществующим id")
    @Description("если отправить запрос с несуществующим id, вернётся ошибка; неуспешный запрос возвращает соответствующую ошибку;")
    public void deleteCourierWithNoExistStatus404()
    {
        courierId = "0";
        courierManager.deleteCourier(courierId).then().assertThat().body("message",equalTo("Курьера с таким id нет.")).statusCode(404);
    }

    @Test
    @DisplayName("Удаление курьера без id")
    @Description("если отправить запрос без id, вернётся ошибка")
    //не получилось
    public void deleteCourierWithoutIdStatus400()
    {
        courierManager.deleteCourier("").then().assertThat().body("message",equalTo("Недостаточно данных для удаления курьера")).statusCode(400);
    }

    @After
    public void tearDown()
    {
        if (courierManager.loginCourier(courier).statusCode() == 200) {
            courierManager.deleteCourier(courierManager.idCourier(courier));
        }
    }
}
