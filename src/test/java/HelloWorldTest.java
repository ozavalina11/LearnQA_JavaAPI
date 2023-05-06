import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

  @Test
  public void testHelloWorld() {
    System.out.println("Hello world");
  }

  @Test
  public void testGetHello() {
    Response response = RestAssured
        .get("https://playground.learnqa.ru/api/hello")
        .andReturn();
    response.prettyPrint();
  }

  @Test
  public void testRestAssured() {
    Map<String, String> params = new HashMap<>();
    params.put("name", "Jhon"); // в params с помощью put можем передать любое количество значений
    Response response = RestAssured // билдер
                                    .given() // объясняем билдеру, что сейчас мы будем прописывать параметры запроса when then
                                    //.queryParam("name", "Jhon")
                                    .queryParams(params)
                                    .get("https://playground.learnqa.ru/api/hello") //сеттер
                                    .andReturn(); // экзекьютер
    response.prettyPrint();
  }

  @Test
  public void testJson() {
    Map<String, String> params = new HashMap<>();
    params.put("name", "Jhon");

    JsonPath response = RestAssured
        .given()
        .queryParams(params)
        .get("https://playground.learnqa.ru/api/hello")
        .jsonPath(); // изменился тип ответа

    String answer = response.get("answer"); // получаем значение по ключу
    System.out.println(answer);
  }

  @Test
  public void testJsonError() {
    Map<String, String> params = new HashMap<>();
    params.put("name", "Jhon");

    JsonPath response = RestAssured
        .given()
        .queryParams(params)
        .get("https://playground.learnqa.ru/api/hello")
        .jsonPath(); // изменился тип ответа

    String answer = response.get("errrrrrrr");
    if (answer == null) {
      System.out.println("The key 'errrrrrrr' is absent");
    } else {
      System.out.println(answer);
    }
  }

  @Test
  public void testGetCheckType() {
    Response response = RestAssured
        .given()
        .body("param1=value1&param2=value2")
        //.queryParam("param1", "value1")
        //.queryParam("param2", "value2")
        //.get("https://playground.learnqa.ru/api/check_type")
        .post("https://playground.learnqa.ru/api/check_type")
        .andReturn();
    response.print();
  }

  @Test
  public void testPostJsonCheckType() {
    Response response = RestAssured
        .given()
        .body("{\"param1\":\"value1\",\"param2\":\"value2\"}")
        .post("https://playground.learnqa.ru/api/check_type")
        .andReturn();
    response.print();
  }

  @Test
  public void testPostJsonCheckType2() {
    Map<String, Object> body =
        new HashMap<>(); // когда исп Map, то передаем  данные именно в json формате
    // но строку json за нас составляет библ RestAssured
    body.put("param1", "value1");
    body.put("param2", "value2");

    Response response = RestAssured
            .given()
            .body(body)
            .post("https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }

  @Test
  public void testGetStatusCode200() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/check_type")
            .andReturn();
    int statusCode = response.getStatusCode();
    System.out.println(statusCode);
  }

  @Test
  public void testGetStatusCode500() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/get_500")
            .andReturn();
    int statusCode = response.getStatusCode();
    System.out.println(statusCode);
  }

  @Test
  public void testGetStatusCode404() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/non-existent_method")
            .andReturn();
    int statusCode = response.getStatusCode();
    System.out.println(statusCode);
  }

  @Test
  public void testGetStatusCode303Redirect() {
    Response response = RestAssured
            .given()
            .redirects()
            //.follow(false) // результат: 303
            .follow(true) // результат: 200
            .when()
            .get("https://playground.learnqa.ru/api/get_303")
            .andReturn();
    int statusCode = response.getStatusCode();
    System.out.println(statusCode);
  }
}
