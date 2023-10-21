package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import ru.netology.diploma.data.SQLHalper;

import java.sql.SQLException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageBuyTicket {
    private final SelenideElement heading = $$("h2.heading  ").find(text("Путешествие дня"));
    private static SelenideElement cardNumberLine = $$(".input__inner").find(text("Номер карты")).$(".input__control");
    private static SelenideElement monthLine = $$(".input__inner").find(text("Месяц")).$(".input__control");
    private static SelenideElement yearLine = $$(".input__inner").find(text("Год")).$(".input__control");
    private static SelenideElement OwnerCardLine = $$(".input__inner").find(text("Владелец")).$(".input__control");
    private static SelenideElement cvcLine = $$(".input__inner").find(text("CVC/CVV")).$(".input__control");
    private static SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    private static SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    private static SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    private static SelenideElement payCreditByCard = $$(".heading").find(exactText("Кредит по данным карты"));
    private static SelenideElement messageSuccessfully = $$(".notification__title").find(exactText("Успешно"));
    private static SelenideElement messageError = $$(".notification__title").find(exactText("Ошибка"));
    private static SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private static SelenideElement incorrectFormat = $(".input_invalid");
    private static SelenideElement requiredField = $$(".input__inner span.input__sub").find(exactText("Поле обязательно для заполнения"));

    public PageBuyTicket () {
        heading.shouldBe(Condition.visible);
    }
    @Step("Купить по карте")
    public static void buyTicket() {
        buyButton.click();
        payCard.shouldBe(Condition.visible);
    }

    @Step("Купить в кредит")
    public static void buyTicketCredit() {
        buyCreditButton.click();
        payCreditByCard.shouldBe(Condition.visible);
    }

    @Step("Заполнить поле 'номер карты'")
    public static void setCardNumber(String cardNumber) {
        cardNumberLine.setValue(cardNumber);
    }

    @Step("Заполние поля 'месяц'")
    public static void setCardMonth(String month) {
        monthLine.setValue(month);
    }

    @Step("Заполние поля 'год'")
    public static void setCardYear(String year) {
        yearLine.setValue(year);
    }

    @Step("Заполние поля 'владелец карты'")
    public static void setCardOwner(String owner) {
        OwnerCardLine.setValue(owner);
    }

    @Step("Заполние поля 'cvc'")
    public static void setCardCVC(String cvc) {
        cvcLine.setValue(cvc);
    }

    @Step("Нажать кнопку 'продолжить'")
    public static void clickContinueButton() {
        continueButton.click();
    }

    @Step("Уведомление успешной операции")
    public static void findMessageSuccessfully() {
        messageSuccessfully.shouldHave(Condition.text("Успешно"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Step("Уведомление об ошибке при совершении операции")
    public static void findMessageError() {
        messageError.shouldHave(Condition.text("Ошибка"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Step("Уведомление о неверно заполненном поле")
    public static void findMessageIncorrectFormat() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    @Step("Уведомление об обязательном заполнении поля")
    public static void findMessageRequiredField() {
        requiredField.shouldBe(Condition.visible);
    }


    // Запросы к базе данных.
    @SneakyThrows
    @Step("Сверка со статусом 'APPROVED'. Раздел 'Купить'")
    public static void verifyStatusWithApprovedBuy()  {
        var status = SQLHalper.verifyOrderPayment();
        assertEquals("APPROVED", status);
    }
    @SneakyThrows
    @Step("Сверка со статусом 'DECLINED'. Раздел 'Купить'")
    public static void verifyStatusWithDeclinedBuy()  {
        var status = SQLHalper.verifyOrderPayment();
        assertEquals("DECLINED", status);
    }
    @SneakyThrows
    @Step("Сверка со статусом 'APPROVED'. Раздел 'Купить в кредит'")
    public static void verifyStatusWithApprovedByCredit()  {
        var status = SQLHalper.verifyOrderCredit();
        assertEquals("APPROVED", status);
    }
    @SneakyThrows
    @Step("Сверка со статусом 'DECLINED'. Раздел 'Купить в кредит'")
    public static void verifyStatusWithDeclinedByCredit()  {
        var status = SQLHalper.verifyOrderCredit();
        assertEquals("DECLINED", status);
    }


}
