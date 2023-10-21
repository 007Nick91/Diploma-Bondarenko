package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.data.SQLHalper;
import ru.netology.diploma.page.PageBuyTicket;

import static com.codeborne.selenide.Selenide.*;



public class TestByTicket {
    PageBuyTicket pageBuyTicket;

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void openSite(){
        pageBuyTicket = open("http://localhost:8080/", PageBuyTicket.class);
    }

//Тесты с корректным заполнением форм Купить и Купить в кредит
    @Test
    @DisplayName("Поля заполнены верно - успешная покупка картой 4441. Проверка статуса и внешнего ключа.")
    void buyByApprovedCard()  {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageSuccessfully();
        PageBuyTicket.verifyStatusWithApprovedBuy();
        SQLHalper.comparIdPaymentAndOrder();

    }

    @Test
    @DisplayName("Поля заполнены верно - успешная покупка в кредит картой 4441. Проверка статуса и внешнего ключа.")
    void buyInCreditByApprovedCard()  {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageSuccessfully();
        PageBuyTicket.verifyStatusWithApprovedByCredit();
        SQLHalper.comparIdCreditAndOrder();
    }

    @Test
    @DisplayName("Поля заполнены верно - отказ в покупке картой 4442. Проверка статуса и внешнего ключа.")
    void rejectBuyByDeclinedCard()  {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getDeclinedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageError();
        PageBuyTicket.verifyStatusWithDeclinedBuy();
    }

    @Test
    @DisplayName("Поля заполнены верно - отказ в покупке в кредит картой 4442. Проверка статуса и внешнего ключа.")
    void rejectBuyInCreditDeclinedCard()  {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getDeclinedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageError();
        PageBuyTicket.verifyStatusWithDeclinedByCredit();
    }

    @Test
    @DisplayName("Поля заполнены верно - отказ в покупке неустановленной картой 4444")
    void rejectBuyNotRegistredCard()  {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getNotRegistredCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageError();

    }

    @Test
    @DisplayName("Поля заполнены верно - отказ в покупке в кредит неустановленной картой 4444")
    void rejectBuyInCreditNotRegistredCard()  {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getNotRegistredCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageError();
    }

    @Test
    @DisplayName("Поля не заполнены. Раздел 'Купить'")
    void shouldErrorTestOfNullByBuy() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber("");
        PageBuyTicket.setCardMonth("");
        PageBuyTicket.setCardYear("");
        PageBuyTicket.setCardOwner("");
        PageBuyTicket.setCardCVC("");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
        PageBuyTicket.findMessageRequiredField();
    }
    @Test
    @DisplayName("Поля не заполнены. Раздел 'Купить в кредит'")
    void shouldErrorTestOfNullByCredit() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber("");
        PageBuyTicket.setCardMonth("");
        PageBuyTicket.setCardYear("");
        PageBuyTicket.setCardOwner("");
        PageBuyTicket.setCardCVC("");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
        PageBuyTicket.findMessageRequiredField();
    }

    // Тесты с вводом некорректных данных в поле Месяц.
    //Раздел Купить
    @Test
    @DisplayName("Ввод букв в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfMonthWithLetters() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("йц");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 00 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfMonthWithZero() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("00");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод -1 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfMonthWithMinus() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("-1");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 99 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfMonthWith99() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("99");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит
    @Test
    @DisplayName("Ввод букв в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfMonthWithLettersCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("йц");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 00 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfMonthWithZeroCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("00");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод -1 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfMonthWithMinusCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("-1");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 99 в поле 'Месяц', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfMonthWith99CreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth("99");
        PageBuyTicket.setCardYear(DataGenerator.generateYear());
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

// Тесты с вводом некорректных данных в поле Год.
//Раздел Купить
    @Test
    @DisplayName("Ввод букв в поле 'Год', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfYearLetters() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getNotRegistredCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("qw");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 00 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfYearWithZero() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("00");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод -1 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfYearWithMinus() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("-1");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 99 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfYearWith99() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("99");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит
    @Test
    @DisplayName("Ввод букв в поле 'Год', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfYearWithLettersCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("as");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 00 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfYearWithZeroCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("00");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод -1 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfYearWithMinusCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("-1");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 99 в поле 'Год', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfYearWith99CreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("99");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    // Тесты с вводом некорректных данных в поле Владелец.
    //Раздел Купить
    @Test
    @DisplayName("Ввод в поле 'Владелец' только Имя, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerJustName() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("Petr");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец' имя и фамилию на русском языке, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerJustRusName() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("Петр Петров");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец'  одну букву, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerJustOneLetter() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("P");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец' цифры, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerNumbers() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("1234");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }

    // Раздел Купить в кредит
    @Test
    @DisplayName("Ввод в поле 'Владелец' только Имя, остальные поля заполнены верно.")
    void shouldErrorTestOfOwnerJustNameCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("Petr");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец' имя и фамилию на русском языке, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerJustRusNameCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("Петр Петров");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец'  одну букву, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerJustOneLetterCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("P");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }
    @Test
    @DisplayName("Ввод в поле 'Владелец' цифры, остальные поля заполнены верно. ")
    void shouldErrorTestOfOwnerNumbersCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner("1234");
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();

    }

    // Тесты с вводом некорректных данных в поле CVC/CVV
    //Раздел Купить
    @Test
    @DisplayName("Ввод букв в поле 'CVC' , остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfCvcByBuy() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("qwe");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Ввод 0 в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfCvcWithZero() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("0");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 000 в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfCvcWithThreeZero() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("000");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод четырех цифр в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить'")
    void shouldErrorTestOfYearWithThousand() {
        PageBuyTicket.buyTicket();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("1000");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

    //Раздел Купить в кредит

    @Test
    @DisplayName("Ввод букв в поле 'CVC' , остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfCvcByBuyCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("qwe");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }

    @Test
    @DisplayName("Ввод 0 в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfCvcWithZeroCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("0");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод 000 в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfCvcWithThreeZeroCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear(DataGenerator.generateYear());;
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC("000");
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
    @Test
    @DisplayName("Ввод четырех цифр в поле 'CVC', остальные поля заполнены верно. Раздел 'Купить в кредит'")
    void shouldErrorTestOfYearWithThousandCreditCard() {
        PageBuyTicket.buyTicketCredit();
        PageBuyTicket.setCardNumber(DataGenerator.getApprovedCard());
        PageBuyTicket.setCardMonth(DataGenerator.getGenMonth());
        PageBuyTicket.setCardYear("1000");
        PageBuyTicket.setCardOwner(DataGenerator.generateOwner());
        PageBuyTicket.setCardCVC(String.valueOf(DataGenerator.getGenCvc()));
        PageBuyTicket.clickContinueButton();
        PageBuyTicket.findMessageIncorrectFormat();
    }
// Проверка записей в БД
    @Test
    @DisplayName("Проверка заполнения полей таблиц БД")
    void verifyDBNotNull()  {
        SQLHalper.verifyPaymentNotNull();
        SQLHalper.verifyCreditNotNull();
    }


}

