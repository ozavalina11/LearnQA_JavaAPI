import io.restassured.RestAssured;
import io.restassured.http.Headers;
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

  @Test
  public void testHeaders1() {
    Map<String, String> headers = new HashMap<>();
    headers.put("myHeader1", "myValue1");
    headers.put("myHeader2", "myValue2");

    Response response = RestAssured
            .given()
            .headers(headers)
            .when()
            .get("https://playground.learnqa.ru/api/show_all_headers")
            .andReturn();
    response.prettyPrint();

    Headers responseHeaders = response.getHeaders();
    System.out.println(responseHeaders);
  }

  @Test
  public void testHeaders2() {
    Map<String, String> headers = new HashMap<>();
    headers.put("myHeader1", "myValue1");
    headers.put("myHeader2", "myValue2");

    Response response = RestAssured
            .given()
            .redirects()
            .follow(false)
            .when()
            .get("https://playground.learnqa.ru/api/get_303")
            .andReturn();
    response.prettyPrint();

    String locationHeader = response.getHeader("Location");
    System.out.println(locationHeader);
  }

  @Test
  public void testCookies() {
    Map<String, String> date = new HashMap<>();
    date.put("login", "secret_login"); // date.put("login", "secret_login2");
    date.put("password", "secret_pass"); // date.put("login", "secret_login2");

    Response response = RestAssured
            .given()
            .body(date)
            .when()
            .post("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

    System.out.println("\nPretty text:");
    response.prettyPrint();

    System.out.println("\nHeaders:");
    Headers responseHeaders = response.getHeaders();
    System.out.println(responseHeaders);

    System.out.println("\nCookies:");
    Map<String, String> responseCookies = response.getCookies();
    System.out.println(responseCookies);
  }

  @Test
  public void testCookie() {
    Map<String, String> date = new HashMap<>();
    date.put("login", "secret_login");
    date.put("password", "secret_pass");

    Response response = RestAssured
            .given()
            .body(date)
            .when()
            .post("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

    String responseCookie = response.getCookie("auth_cookie");
    System.out.println(responseCookie);
  }

  @Test
  public void testGetCookie() {
    Map<String, String> date = new HashMap<>();
    date.put("login", "secret_login");
    date.put("password", "secret_pass");

    Response responseForGet = RestAssured
            .given()
            .body(date)
            .when()
            .post("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

    //получили куки
    String responseCookie = responseForGet.getCookie("auth_cookie");

    // передаем куки
    Map<String, String> cookies = new HashMap<>();
    if (responseCookie != null) {
      cookies.put("auth_cookie", responseCookie);
    }

    Response responseForCheck = RestAssured
            .given()
            .body(date)
            .cookies(cookies)
            .when()
            .post("https://playground.learnqa.ru/api/check_auth_cookie")
            .andReturn();

    responseForCheck.print();
  }
}
