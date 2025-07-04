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

public class ReplanDeliveryTestV1 {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldReplanMeetingDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstPlanningDate = DataGenerator.generateDate(5);
        var secondPlanningDate = DataGenerator.generateDate(9);

        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().press(Keys.DELETE).setValue(firstPlanningDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .should(Condition.text("Успешно! Встреча успешно запланирована на " + firstPlanningDate), Duration.ofSeconds(15))
                .should(Condition.visible);
        $("[data-test-id='date'] input").doubleClick().press(Keys.DELETE).setValue(secondPlanningDate);
        $$("button").findBy(Condition.text("Запланировать")).click();
        $("[data-test-id='replan-notification']")
                .should(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .should(Condition.visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification']")
                .should(Condition.text("Успешно! Встреча успешно запланирована на " + secondPlanningDate), Duration.ofSeconds(15))
                .should(Condition.visible);
    }
}
