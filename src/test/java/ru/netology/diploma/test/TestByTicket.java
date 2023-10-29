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
// Не существующая карта
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
// Пустые поля
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
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering Zero in the 'Month' field, the other fields are filled in correctly. Section 'Buy on section'")
    void shouldErrorTestOfMonthWithZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectZero());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }



    @Test
    @DisplayName("Enter -1 in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithNegative() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNegative());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter incorrect Number in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithIncorNumber() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Space in the 'Month' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfMonthWithSpace() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectSpace());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


    //

    //Раздел Купить в кредит
    @Test
    @DisplayName("Entering letters in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering Zero in the 'Month' field, the other fields are filled in correctly. Section 'Buy on credit'")
    void shouldErrorTestOfMonthWithZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectZero());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter -1 in the 'Month' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfMonthWithNegativeCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNegative());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter incorrect Number in the 'Month' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfMonthWithIncorNumberCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Space in the 'Month' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfMonthWithSpaceCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateIncorrectSpace());
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
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Zero in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearLettersZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectZero());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


@Test
@DisplayName("Enter Negative in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithNegative() {
            pageBuyTicket.buyTicket();
            pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
            pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
            pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNegative());
            pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
            pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
            pageBuyTicket.clickContinueButton();
            pageBuyTicket.findMessageIncorrectFormat();
            }

@Test
@DisplayName("Enter incorrect Number in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithMinus() {
            pageBuyTicket.buyTicket();
            pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
            pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
            pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNumber());
            pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
            pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
            pageBuyTicket.clickContinueButton();
            pageBuyTicket.findMessageIncorrectFormat();
            }

@Test
@DisplayName("Enter Space in the 'Year' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfYearWithSpace() {
            pageBuyTicket.buyTicket();
            pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
            pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
            pageBuyTicket.setCardYear(DataGenerator.generateIncorrectSpace());
            pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
            pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
            pageBuyTicket.clickContinueButton();
            pageBuyTicket.findMessageIncorrectFormat();
            }


    //Раздел Купить в кредит

    @Test
    @DisplayName("Enter letters in the 'Year' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfYearLettersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Zero in the 'Year' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfYearLettersZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectZero());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }


    @Test
    @DisplayName("Enter Negative in the 'Year' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfYearWithNegativeCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNegative());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter incorrect Number in the 'Year' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfYearWithMinusCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Space in the 'Year' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfYearWithSpaceCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateIncorrectSpace());
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

    @Test
    @DisplayName("Enter the first and last name in the 'Owner' field in Russian, the other fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustRusName() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectRus());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Enter one letter in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustOneLetter() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering numbers in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerNumbers() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering Symbols in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerSymbols() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectSymbols());
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

    @Test
    @DisplayName("Enter the first and last name in the 'Owner' field in Russian, the other fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustRusNameCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectRus());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Enter one letter in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerJustOneLetterCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectletters());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering numbers in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerNumbersCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.setCardCVC(String.valueOf(DataGenerator.generateCvc()));
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();

    }

    @Test
    @DisplayName("Entering Symbols in the 'Owner' field, the rest of the fields are filled in correctly.")
    void shouldErrorTestOfOwnerSymbolsCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateIncorrectSymbols());
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
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectletters());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering Zero in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcByBuyZero() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectZero());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter incorrect Number in the 'CVC' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcWithInNumber() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Space in the 'CVC' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcWithThreeSpace() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectSpace());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Negative in the 'CVC' field, the other fields are filled in correctly. The 'Buy' section")
    void shouldErrorTestOfCvcWithThreeNegative() {
        pageBuyTicket.buyTicket();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNegative());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит
        @Test
    @DisplayName("Entering letters in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfCvcByBuyCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectletters());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Entering Zero in the 'CVC' field, the rest of the fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfCvcByBuyZeroCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectZero());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter incorrect Number in the 'CVC' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfCvcWithInNumberCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNumber());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Space in the 'CVC' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfCvcWithThreeSpaceCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectSpace());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Enter Negative in the 'CVC' field, the other fields are filled in correctly. The 'Buy' credit")
    void shouldErrorTestOfCvcWithThreeNegativeCreditCard() {
        pageBuyTicket.buyTicketCredit();
        pageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        pageBuyTicket.setCardMonth(DataGenerator.generateMonth());
        pageBuyTicket.setCardYear(DataGenerator.generateYear());
        pageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        pageBuyTicket.setCardCVC(DataGenerator.generateIncorrectNegative());
        pageBuyTicket.clickContinueButton();
        pageBuyTicket.findMessageIncorrectFormat();
    }

}

