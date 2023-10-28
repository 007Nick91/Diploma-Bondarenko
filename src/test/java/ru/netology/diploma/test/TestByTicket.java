package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.data.SQLHalper;
import ru.netology.diploma.page.PageBuyTicket;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestByTicket {
    PageBuyTicket pageBuyTicket;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openSite() {
        pageBuyTicket = open("http://localhost:8080/", PageBuyTicket.class);
    }


    //Тесты с корректным заполнением форм Купить и Купить в кредит и запросы к базе данных (Проверка статуса и внешнего ключа)
    @Test
    @DisplayName("The fields are filled in correctly - successful purchase by card 4441. Checking the status and the foreign key.")
    void buyByApprovedCard() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageSuccessfully("Успешно");
        var status = SQLHalper.verifyOrderPayment();
        var payment = SQLHalper.comparIdPayment();
        var order = SQLHalper.comparIdOrder();
        assertEquals("APPROVED", status);
        assertEquals(payment, order, "Payment and Order IDs are not equal");

    }

    @Test
    @DisplayName("The fields are filled in correctly - successful purchase on credit card 4441. Checking the status and the foreign key.")
    void buyInCreditByApprovedCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageSuccessfully("Успешно");
        var status = SQLHalper.verifyOrderCredit();
        var credit = SQLHalper.comparIdCredit();
        var order = SQLHalper.comparIdOrder();
        assertEquals("APPROVED", status);
        assertEquals(credit, order, "Credit and Order IDs are not equal");
    }

    @Test
    @DisplayName("The fields are filled in correctly - refusal to purchase by card 4442. Checking the status and the foreign key.")
    void rejectBuyByDeclinedCard() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getDeclinedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageError("Ошибка");
        var status = SQLHalper.verifyOrderPayment();
        assertEquals("DECLINED", status);

    }

    @Test
    @DisplayName("The fields are filled in correctly - refusal to purchase with a credit card 4442. Checking the status and the foreign key.")
    void rejectBuyInCreditDeclinedCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getDeclinedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageError("Ошибка");
        var status = SQLHalper.verifyOrderCredit();
        assertEquals("DECLINED", status);
    }

    @Test
    @DisplayName("The fields are filled in correctly - refusal to purchase with an unidentified card 4444")
    void rejectBuyNotRegistredCard() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getNotRegistredCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageError("Ошибка");

    }

    @Test
    @DisplayName("The fields are filled in correctly - refusal to purchase on credit with an unidentified card 4444")
    void rejectBuyInCreditNotRegistredCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getNotRegistredCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageError("Ошибка");
    }

    @Test
    @DisplayName("The fields are not filled in. The 'Buy' section")
    void shouldErrorTestOfNullByBuy() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber("");
        pageBuyTicket.setCardMonth("");
        pageBuyTicket.setCardYear("");
        pageBuyTicket.setCardOwner("");
        pageBuyTicket.setCardCVC("");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
        pageBuyTicket.findMessageRequiredField();
    }

    @Test
    @DisplayName("The fields are not filled in. Section 'Buy on credit'")
    void shouldErrorTestOfNullByCredit() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber("");
        pageBuyTicket.setCardMonth("");
        pageBuyTicket.setCardYear("");
        pageBuyTicket.setCardOwner("");
        pageBuyTicket.setCardCVC("");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
        pageBuyTicket.findMessageRequiredField();
    }

    // Тесты с вводом некорректных данных в поле Месяц.
    //Раздел Купить
    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithLetters() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithLettersCardZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("00");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит
    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithLettersCreditCardZero() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("00");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


    // Тесты с вводом некорректных данных в поле Год.
    //Раздел Купить
    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearLetters() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearLettersZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("00");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


    //Раздел Купить в кредит
    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWithLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithLettersCreditCardZero() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("00");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


    // Тесты с вводом некорректных данных в поле Владелец.
    //Раздел Купить
    @Test
    @DisplayName("Entering only the Name in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustName() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    //     Раздел Купить в кредит
    @Test
    @DisplayName("Entering only the Name in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustNameCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    // Тесты с вводом некорректных данных в поле CVC/CVV
    //Раздел Купить
    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcByBuy() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcByBuyZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("000");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит
    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfCvcByBuyCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectName());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcByCreditZero() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("000");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

}

