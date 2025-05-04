package ru.netology.replan.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.replan.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReplanDeliveryTestV2 {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldGetErrorWhenInputWrongPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var planningDate = DataGenerator.generateDate(5);
        var invalidPhone = DataGenerator.generateInvalidPhone("ru");

        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().press(Keys.DELETE).setValue(planningDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidPhone);
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub").should(Condition.text("Номер телефона указан неверно"));
    }
}
