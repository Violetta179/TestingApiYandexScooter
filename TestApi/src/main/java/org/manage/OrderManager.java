package org.manage;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderManager {
    String endpointOrders = "/api/v1/orders";
    String endpointAcceptOrders = "/api/v1/orders/accept/";
    String endpointOrdersTrack = "/api/v1/orders/track";
    @Step("Создание заказа")
    public Response createOrder(Order order)
    {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(endpointOrders);
    }

    @Step("Получить список заказов")
    public Response orderList(String courierId)
    {
        return given()
                .header("Content-type", "application/json")
                // .queryParam("courierId",courierId), дальше у тебя такая конструкция используется, а здесь почему нет?
                .get(endpointOrders+"?courierId="+courierId);
    }

    @Step("Принять заказ")
    public Response acceptOrder(String orderId, String courierId)
    {
        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId",courierId)
            // если сделаем String endpointAcceptOrders = "/api/v1/orders/accept/{id}";, то можно будет использовать
            // .pathParam("id", orderId)
            // .put(endpointAcceptOrders);
                .put(endpointAcceptOrders+orderId);
    }

    @Step("Принять заказ без номера ")
    public Response acceptOrderWithoutNumberOrder(String courierId)
    {
        return given()
                .header("Content-type", "application/json")
                .put(endpointAcceptOrders+courierId);
    }
    @Step("Получить заказ по его номеру")
    public Response acceptOrderNumber(String track)
    {
        return given()
                .header("Content-type", "application/json").queryParam("t",track).get(endpointOrdersTrack);
    }

    @Step("Отменить заказ")
    public Response cancelOrder(String track)
    {
        return given()
                .header("Content-type", "application/json")
            // Map.of("track", track) - это тоже самое, что и "{\"track\": \"" + track + "\"}"
                .body("{\n" +
                        "    \"track\":" +track +
                        "}")
                .when()
                .put(endpointOrders);
    }
    @Step("Получить номер track")
    public String extractTrack(Order order)
    {
        return createOrder(order).then().extract().path("track").toString();
    }
}
