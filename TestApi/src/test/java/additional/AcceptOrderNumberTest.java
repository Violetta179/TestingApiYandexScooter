package additional;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import org.pojo.Order;
import org.manage.OrderManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
public class AcceptOrderNumberTest {
    String track;
    String firstName = "saske";
    String lastName="Uchiha";
    String address= "Konocha";
    int metroStation =4;
    String phone ="+7 800 355 35 3";
    int rentTime=5;
    String deliveryDate="2020-06-06";
    String comment="Good day";
    String[] color= new String[]{"BACK"};
    private OrderManager orderManager;
    Order order;


    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";

        orderManager = new OrderManager();
        order = new Order(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
        orderManager.createOrder(order);
    }


    @Test
    @DisplayName("Получить заказ по номеру и получить стаус код 200")
    @Description("успешный запрос возвращает объект с заказом;")
    public void receiveOrderNumberStatus200()
    {
        track = orderManager.extractTrack(order);
        orderManager.acceptOrderNumber(track).then().statusCode(200);
    }

    @Test
    @DisplayName("Получить заказ без номера")
    @Description("запрос без номера заказа возвращает ошибку;")
    public void receiveOrderWithoutStatus400()
    {
        // делай переносы на новую строку, чтобы код легче читался,
        // избегай слишком длинных строк
        // статус код лучше в начале проверять, а потом уже тело ответа
        orderManager.acceptOrderNumber("").then().body("message",equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @Test
    @DisplayName("Получить заказ с несуществующим номером")
    @Description("запрос с несуществующим заказом возвращает ошибку.")
    public void receiveOrderWithNoExistNumberStatus404()
    {
        orderManager.acceptOrderNumber("1").then().body("message",equalTo("Заказ не найден")).statusCode(404);
    }

    @After
    public void tearDown()
    {
        orderManager.cancelOrder(track);
    }

}
