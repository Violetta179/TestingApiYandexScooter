package org.manage;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.HashMap;
import org.pojo.Courier;

import static io.restassured.RestAssured.given;


public class CourierManager {
    String endpointCreateCourier = "/api/v1/courier";
    String endpointLoginCourier = "/api/v1/courier/login";
    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                // можно заменить на .contentType(ContentType.JSON)
                .header("Content-type", "application/json")
                .body(courier)
                .post(endpointCreateCourier);
    }

    @Step("Авторизировать курьера")
    public Response loginCourier(Courier courier) {
        return given()
                // можно заменить на .contentType(ContentType.JSON)
                .header("Content-type", "application/json")
                .body(courier)
                .post(endpointLoginCourier);
    }

    @Step("Удалить курьера")
    // название метода можно уточнить, например, deleteCourierById
    public Response deleteCourier(String id) {
        return given()
                // можно заменить на .contentType(ContentType.JSON)
                .header("Content-type", "application/json")
                // можно заменить на .body(Map.of("id", id))
                .body("{\"id\": \"" + id + "\"}")
                .when()
                .delete(endpointCreateCourier +"/"+ id);
    }

    @Step("Получить id курьера")
    // тут лучше назвать getIdCourier, чтобы было понятно, что возврат id
    public String idCourier(Courier courier) {
        return loginCourier(courier).then().extract().path("id").toString();
    }

}
