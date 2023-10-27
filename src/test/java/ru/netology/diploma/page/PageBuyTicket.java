package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PageBuyTicket {
    private final SelenideElement heading = $$("h2.heading  ").find(text("Путешествие дня"));
    private SelenideElement cardNumberLine = $$(".input__inner").find(text("Номер карты")).$(".input__control");
    private SelenideElement monthLine = $$(".input__inner").find(text("Месяц")).$(".input__control");
    private  SelenideElement yearLine = $$(".input__inner").find(text("Год")).$(".input__control");
    private SelenideElement OwnerCardLine = $$(".input__inner").find(text("Владелец")).$(".input__control");
    private SelenideElement cvcLine = $$(".input__inner").find(text("CVC/CVV")).$(".input__control");
    private SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    private SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    private SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    private SelenideElement payCreditByCard = $$(".heading").find(exactText("Кредит по данным карты"));
    private SelenideElement messageSuccessfully = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement messageError = $$(".notification__title").find(exactText("Ошибка"));
    private SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private SelenideElement incorrectFormat = $(".input_invalid");
    private SelenideElement requiredField = $$(".input__inner span.input__sub").find(exactText("Поле обязательно для заполнения"));

    public PageBuyTicket() {
        heading.shouldBe(Condition.visible);
    }

    @Step("Купить по карте")
    public void buyTicket() {
        buyButton.click();
        payCard.shouldBe(Condition.visible);
    }

    @Step("Купить в кредит")
    public void buyTicketCredit() {
        buyCreditButton.click();
        payCreditByCard.shouldBe(Condition.visible);
    }

    @Step("Заполнить поле 'номер карты'")
    public void setCardNumber(String cardNumber) {
        cardNumberLine.setValue(cardNumber);
    }

    @Step("Заполние поля 'месяц'")
    public void setCardMonth(String month) {
        monthLine.setValue(month);
    }

    @Step("Заполние поля 'год'")
    public void setCardYear(String year) {
        yearLine.setValue(year);
    }

    @Step("Заполние поля 'владелец карты'")
    public void setCardOwner(String owner) {
        OwnerCardLine.setValue(owner);
    }

    @Step("Заполние поля 'cvc'")
    public void setCardCVC(String cvc) {
        cvcLine.setValue(cvc);
    }

    @Step("Нажать кнопку 'продолжить'")
    public void clickContinueButton() {
        continueButton.click();
    }

    @Step("Уведомление успешной операции")
    public void findMessageSuccessfully(String expextedText) {
        messageSuccessfully.shouldHave(Condition.text(expextedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Step("Уведомление об ошибке при совершении операции")
    public void findMessageError(String expextedText) {
        messageError.shouldHave(Condition.text(expextedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Step("Уведомление о неверно заполненном поле")
    public void findMessageIncorrectFormat(String expextedText) {
        incorrectFormat.shouldHave(Condition.text(expextedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Step("Уведомление об обязательном заполнении поля")
    public void findMessageRequiredField(String expextedText) {
        requiredField.shouldHave(Condition.text(expextedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }


}
