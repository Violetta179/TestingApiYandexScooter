package testbasic;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import org.pojo.Order;
import org.manage.OrderManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    String firstName;
    String lastName;
    String address;
    int metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    String[] color;
    String track;
    private OrderManager orderManager;
    private Order order;


    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Naruto","Uzumaki","Konocha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha",new String[]{"BLACK"}},
                {"Sakura","Charuno","Konocha, 140 apt.",4,"+7 800 355 35 35",4,"2021-06-06","Saske, come back to Konoha",new String[]{"GREY"}},
                {"Naruto","Uzumaki","Konocha, 141 apt.",4,"+7 800 355 35 35",5,"2022-06-06","Saske, come back to Konoha",new String[]{"BLACK,GREY"}},
                {"Sakura","Charuno","Konocha, 140 apt.",4,"+7 800 355 35 35",5,"2023-06-06","Saske, come back to Konoha",new String[]{}}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI ="https://qa-scooter.praktikum-services.ru";
        orderManager = new OrderManager();
    }

    @Test
    @DisplayName("Создание заказа с указаени цветов или без них ,получения статус кода 201")
    @Description("можно указать один из цветов — BLACK или GREY;\n" +
            "можно указать оба цвета;\n" +
            "можно совсем не указывать цвет;\n" +
            "тело ответа содержит track.")
    public void createOrderTrack() {
        order = new Order(firstName, lastName, address, metroStation,phone,rentTime,deliveryDate,comment,color);
        orderManager.createOrder(order)
                .then()
                .statusCode(201)
          .body("track", notNullValue());
    }

    @After
    public void tearDown()
    {
      track = orderManager.extractTrack(order);
      orderManager.cancelOrder(track);
    }
}
