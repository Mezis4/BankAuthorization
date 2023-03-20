package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static utility.DataGenerator.Registration.getRegisteredUser;
import static utility.DataGenerator.Registration.getUser;
import static utility.DataGenerator.getRandomLogin;
import static utility.DataGenerator.getRandomPassword;

public class AuthorizationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterEach
    void clear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//span[@data-test-id='login']//child::input").val(registeredUser.getLogin());
        $x("//span[@data-test-id='password']//child::input").val(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//h2").shouldHave(Condition.text("кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//span[@data-test-id='login']//child::input").val(notRegisteredUser.getLogin());
        $x("//span[@data-test-id='password']//child::input").val(notRegisteredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//span[@data-test-id='login']//child::input").val(wrongLogin);
        $x("//span[@data-test-id='password']//child::input").val(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//span[@data-test-id='login']//child::input").val(registeredUser.getLogin());
        $x("//span[@data-test-id='password']//child::input").val(wrongPassword);
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//span[@data-test-id='login']//child::input").val(blockedUser.getLogin());
        $x("//span[@data-test-id='password']//child::input").val(blockedUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if blocked login with wrong password")
    void shouldGetErrorWrongPasswordIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        var wrongPassword = getRandomPassword();
        $x("//span[@data-test-id='login']//child::input").val(blockedUser.getLogin());
        $x("//span[@data-test-id='password']//child::input").val(wrongPassword);
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if blocked login with wrong login")
    void shouldGetErrorWrongLoginIfBlockedUser(){
        var blockedUser = getRegisteredUser("blocked");
        var wrongLogin = getRandomLogin();
        $x("//span[@data-test-id='login']//child::input").val(wrongLogin);
        $x("//span[@data-test-id='password']//child::input").val(blockedUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']//child::div[@class='notification__content']").
                shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should show error under password field if it's empty")
    void shouldShowErrorIfPasswordEmpty() {
        var registeredUser = getRegisteredUser("active");
        $x("//span[@data-test-id='login']//child::input").val(registeredUser.getLogin());
        $x("//button[@data-test-id='action-login']").click();
        $x("//span[@data-test-id='password']//child::span[@class='input__sub']").
                shouldHave(Condition.text("Поле обязательно"));
    }

    @Test
    @DisplayName("Should show error under login field if it's empty")
    void shouldShowErrorIfLoginEmpty() {
        var registeredUser = getRegisteredUser("active");
        $x("//span[@data-test-id='password']//child::input").val(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//span[@data-test-id='login']//child::span[@class='input__sub']").
                shouldHave(Condition.text("Поле обязательно"));
    }

    @Test
    @DisplayName("Should show error under login and password fields if it's empty")
    void shouldShowErrorIfLoginPasswordEmpty() {
        $x("//button[@data-test-id='action-login']").click();
        $x("//span[@data-test-id='password']//child::span[@class='input__sub']").
                shouldHave(Condition.text("Поле обязательно"));
        $x("//span[@data-test-id='login']//child::span[@class='input__sub']").
                shouldHave(Condition.text("Поле обязательно"));
    }
}
