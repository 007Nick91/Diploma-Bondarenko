package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.data.SQLHalper;
import ru.netology.diploma.page.PageBuyTicket;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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

    // Проверка записей в БД
    @Test
    @DisplayName("Checking the fields in the payment_entity and order_entity")
    void verifyDBNotNullPaymentAndOrder() {
        var payment = SQLHalper.verifyPaymentNotNull();
        var order = SQLHalper.verifyOrderNotNull();
        assertNotNull(payment);
        assertNotNull(order);
    }

    @Test
    @DisplayName("Checking the fields in the credit_request_entity and order_entity")
    void verifyDBNotNullCreditAndOrder() {
        var credit = SQLHalper.verifyCreditNotNull();
        var order = SQLHalper.verifyOrderNotNull();
        assertNotNull(credit);
        assertNotNull(order);
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
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
        pageBuyTicket.findMessageRequiredField("Поле обязательно для заполнения");
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
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
        pageBuyTicket.findMessageRequiredField("Поле обязательно для заполнения");
    }

    //    // Тесты с вводом некорректных данных в поле Месяц.
//    //Раздел Купить
    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithLetters() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("йц");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 00 in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("00");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Enter -1 in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithMinus() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("-1");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 13 in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWith13() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("13");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    //
//    //Раздел Купить в кредит
    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("йц");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 00 in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("00");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Enter -1 in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithMinusCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("-1");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 13 in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWith13CreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth("13");
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    //// Тесты с вводом некорректных данных в поле Год.
//Раздел Купить
    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearLetters() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("qw");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 00 in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("00");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Enter 22 in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWith22() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("22");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Enter -1 in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithMinus() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("-1");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 29 in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWith29() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("29");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    //
//    //Раздел Купить в кредит
    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWithLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("as");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 00 in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWithZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("00");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Enter 22 in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWith22CreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("22");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Enter -1 in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWithMinusCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("-1");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 29 in the 'Year' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfYearWith29CreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear("29");
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверно указан срок действия карты");
    }

    //    // Тесты с вводом некорректных данных в поле Владелец.
//    //Раздел Купить
    @Test
    @DisplayName("Entering only the Name in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustName() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("Petr");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter the first and last name in the 'Owner' field in Russian, the other fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustRusName() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("Петр Петров");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    @Test
    @DisplayName("Enter one letter in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustOneLetter() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("P");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    @Test
    @DisplayName("Entering numbers in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerNumbers() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("1234");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    //     Раздел Купить в кредит
    @Test
    @DisplayName("Entering only the Name in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustNameCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("Petr");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    @Test
    @DisplayName("Enter the first and last name in the 'Owner' field in Russian, the other fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustRusNameCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("Петр Петров");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    @Test
    @DisplayName("Enter one letter in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustOneLetterCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("P");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    @Test
    @DisplayName("Entering numbers in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerNumbersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner("1234");
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");

    }

    //    // Тесты с вводом некорректных данных в поле CVC/CVV
//    //Раздел Купить
    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcByBuy() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("qwe");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 0 in the 'CVC' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcWithZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("0");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 000 in the 'CVC' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcWithThreeZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("000");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }


    //    //Раздел Купить в кредит
//
    @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfCvcByBuyCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("qwe");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 0 in the 'CVC' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfCvcWithZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("0");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

    @Test
    @DisplayName("Enter 000 in the 'CVC' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfCvcWithThreeZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC("000");
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat("Неверный формат");
    }

}

