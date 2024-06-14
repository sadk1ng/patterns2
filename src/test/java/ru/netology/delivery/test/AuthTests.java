package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import static ru.netology.delivery.test.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.delivery.test.DataGenerator.Registration.getUser;
import static ru.netology.delivery.test.DataGenerator.getRandomLogin;
import static ru.netology.delivery.test.DataGenerator.getRandomPassword;

public class AuthTests {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should login with active registered user")
    void shouldLoginWithActiveRegisteredUser() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(exactText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error massage if login with not registered user")
    void shouldGetErrorIfLoginIsNotRegistered() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Пользователь заблокирован"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").shouldHave(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));

    }
}