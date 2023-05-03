//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package additional;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.pojo.Courier;
import org.manage.CourierManager;
import org.pojo.Order;
import org.manage.OrderManager;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcceptOrderTest {
    private Courier courier;
    private CourierManager courierManager;
    private String courierId;
    private OrderManager orderManager;
    private Order order;
    private String orderId;
    private String track;
    private final String login = "Violetta";
    private final String password = "1234";
    private final String firstName = "saske";
    String lastName = "Uchiha";
    String address = "Konocha";
    int metroStation = 4;
    String phone = "+7 800 355 35 3";
    int rentTime = 5;
    String deliveryDate = "2020-06-06";
    String comment = "Good day";
    String[] color = new String[]{"BACK"};


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courier = new Courier(login, password, firstName);
        courierManager = new CourierManager();
        courierManager.createCourier(courier);

        order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        orderManager = new OrderManager();
        orderManager.createOrder(order);
    }

    @Test
    @DisplayName("Принять заказ, который возвращает статус 200")
    @Description("успешный запрос возвращает ok: true")
    public void acceptOrderWithIdStatus200() {
        courier = new Courier(login, password);
        courierId = courierManager.idCourier(courier);

        track = orderManager.extractTrack(order);
        orderId = orderManager.acceptOrderNumber(track).then().extract().path("order.id").toString();
        orderManager.acceptOrder(orderId, courierId).then().assertThat().body("ok", Matchers.equalTo(true)).statusCode(200);
    }

    @Test
    @DisplayName("Принять заказ без id курьера")
    @Description("если не передать id курьера, запрос вернёт ошибку;")
    public void acceptOrderWithoutIdStatus400() {
        courierId = "";

        track = orderManager.extractTrack(order);
        orderId = orderManager.acceptOrderNumber(track).then().extract().path("order.id").toString();
        orderManager.acceptOrder(orderId, courierId).then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @Test
    @DisplayName("Принять заказ с неверным id")
    @Description("если передать неверный id курьера, запрос вернёт ошибку;")
    public void acceptOrderIncorrectCourierIdStatus404() {
        courierId = "1";

        track = orderManager.extractTrack(order);
        orderId = orderManager.acceptOrderNumber(track).then().extract().path("order.id").toString();
        orderManager.acceptOrder(orderId, courierId).then().assertThat().body("message", Matchers.equalTo("Курьера с таким id не существует")).statusCode(404);
    }

    @Test
    @DisplayName("Принять заказ с неверным номером")
    @Description("если передать неверный номер заказа, запрос вернёт ошибку")
    public void acceptOrderIncorrectOrderIdStatus404() {
        courier = new Courier(login, password);
        courierId = courierManager.idCourier(courier);

        orderId = "0";
        orderManager.acceptOrder(orderId, courierId).then().assertThat().body("message", Matchers.equalTo("Заказа с таким id не существует")).statusCode(404);
    }

    @Test
    @DisplayName("Принять заказ без номера")
    @Description("если не передать номер заказа, запрос вернёт ошибку;")
    public void acceptOrderWithoutOrderIdStatus400() {
        courier = new Courier(login, password);
        courierId = courierManager.idCourier(courier);

        orderManager.acceptOrderWithoutNumberOrder(courierId).then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @After
    public void tearDown() {
        courierManager.deleteCourier(courierId);
        orderManager.cancelOrder(track);
    }
}
