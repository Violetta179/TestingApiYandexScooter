package testbasic;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import org.pojo.Courier;
import org.manage.CourierManager;
import org.pojo.Order;
import org.manage.OrderManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class ReceiveListOrdersTest {
    private Courier courier;
    private CourierManager courierManager;
    String courierId;
    private OrderManager orderManager;
    private Order order;
    private String orderId;
    private String track;
    private final String login = "Violetta";
    private final String password = "1234";
    private final String firstName = "saske";
    String lastName="Uchiha";
    String address= "Konocha";
    int metroStation =4;
    String phone ="+7 800 355 35 3";
    int rentTime=5;
    String deliveryDate="2020-06-06";
    String comment="Good day";
    String[] color= new String[]{"BACK"};


    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";

        courier = new Courier(login, password,firstName);
        courierManager = new CourierManager();
        courierManager.createCourier(courier);

        order = new Order(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
        orderManager = new OrderManager();
        orderManager.createOrder(order);

    }

    @Test
    @DisplayName("Получить список заказов")
    @Description("Проверь, что в тело ответа возвращается список заказов.")
    public void listOrders()
    {
        courier = new Courier(login, password);
        courierManager.loginCourier(courier);
        courierId = courierManager.idCourier(courier);

        track = orderManager.extractTrack(order);
        orderId = orderManager.acceptOrderNumber(track).then().extract().path("order.id").toString();
        orderManager.acceptOrder(orderId,courierId);

        orderManager.orderList(courierId).then().assertThat().body("orders", hasSize(greaterThan(0))).statusCode(200);
    }

    @After
    public void tearDown()
    {
        courierId = courierManager.idCourier(courier);
        courierManager.deleteCourier(courierId);

        track = orderManager.extractTrack(order);
        orderManager.cancelOrder(track);
    }
}
