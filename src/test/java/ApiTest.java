import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    public void registrationAndAuth() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String json = "{\"email\": \"" + email + "\", \"password\": \"aaa\" }";
        //Зарегистрироваться в Mesto.
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signup")
                .then().statusCode(201);
        //Авторизоваться с теми же параметрами.
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signin");

        response.then().assertThat().body("token", notNullValue())
                .and().statusCode(200);
       // Попытка зарегистрироваться с теми же параметрами ещё раз
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signup")
                .then()
                // проверь, что статус ответа изменился
                .statusCode(409);
    }
}

//Ты проверишь регистрацию и авторизацию в сервисе Mesto. Должен получиться такой сценарий:
//Зарегистрироваться в Mesto.
//Авторизоваться с теми же параметрами.
//Попробовать зарегистрироваться с теми же параметрами ещё раз.
//Это один большой тест, но ты будешь писать его постепенно — по одному запросу за раз. Сначала проверишь регистрацию, потом добавишь авторизацию и попытку регистрации с данными, которые уже существуют.
//У тебя есть две ручки типа POST. Для регистрации — /signup. Для авторизации — /signin. В теле обоих запросов нужно передать такой JSON:
//{
//  "email": "какой-то email",
//  "password": "password"
//}
//Помимо вызовов, нужно проверить:
//Статус ответа при успешной регистрации — 201.
//Статус ответа при успешной авторизации — 200.
//При успешной авторизации в теле ответа приходит токен token. Он не пустой.
//При попытке регистрации с почтой, которая уже есть, статус ответа — 409.
//Обрати внимание: нужно сделать так, чтобы у каждого нового пользователя была уникальная почта, иначе тест будет падать. Поможет генерация случайного числа. Посмотри, как это делается:
//        Random random = new Random();
//        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
//В строке email получится something32423435@yandex.ru. Это значение с высокой вероятностью будет разным при каждом запуске.
//Напиши первый запрос — он проверяет регистрацию. Остальные запросы ты напишешь в следующих заданиях.