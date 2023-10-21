package ru.netology.diploma.data;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SQLHalper {
    private static final QueryRunner runner = new QueryRunner();
    private SQLHalper(){
    }

    private static Connection getConnect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }
    //Проверка наличия записи в базе о покупке с выводом статуса карты
    @SneakyThrows
    public static String verifyOrderPayment(){
        var codeSQL =
                "SELECT payment_entity.status FROM payment_entity " +
                "JOIN order_entity on payment_entity.transaction_id = order_entity.payment_id " +
                "ORDER BY payment_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return String.valueOf(code);
    }
    //Проверка наличия записи в базе о покупке в кредит с выводом статуса карты
    @SneakyThrows
    public static String verifyOrderCredit()  {
        var codeSQL =
                "SELECT credit_request_entity.status FROM credit_request_entity " +
                "JOIN order_entity on credit_request_entity.bank_id = order_entity.payment_id " +
                "ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return String.valueOf(code);
    }

    @SneakyThrows
    @Step("Проверка наличия записей в таблице payment_entity")
    public static void verifyPaymentNotNull()  {
        var codeChekLineOrder = "SELECT * FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var codeChekLinePayment = "SELECT * FROM payment_entity ORDER BY payment_entity.created DESC LIMIT 1;";
        var orderData = runner.query(getConnect(), codeChekLineOrder, new ScalarHandler<>());
        var paymentData = runner.query(getConnect(), codeChekLinePayment, new ScalarHandler<>());
        assertNotNull(orderData);
        assertNotNull(paymentData);
    }
    @SneakyThrows
    @Step("Проверка наличия записей в таблице credit_request_entity")
    public static void verifyCreditNotNull()  {
        var codeChekLineOrder = "SELECT * FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var codeChekLineCredit = "SELECT * FROM credit_request_entity ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var orderData = runner.query(getConnect(), codeChekLineOrder, new ScalarHandler<>());
        var creditData = runner.query(getConnect(), codeChekLineCredit, new ScalarHandler<>());
        assertNotNull(orderData);
        assertNotNull(creditData);
    }
    @SneakyThrows
    @Step("Сравнение внешних ключей в таблицах payment_entity и order_entity")
    public static void comparIdPaymentAndOrder()  {
        var codeOrderIdWithPay = "SELECT payment_id FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var codePaymentId ="SELECT transaction_id FROM payment_entity ORDER BY payment_entity.created DESC LIMIT 1;";
        var orderData = runner.query(getConnect(), codeOrderIdWithPay, new ScalarHandler<>());
        var paymentData = runner.query(getConnect(), codePaymentId, new ScalarHandler<>());
        assertEquals(paymentData, orderData, "Payment and Order IDs are not equal");
    }
    @SneakyThrows
    @Step("Сравнение внешних ключей в таблицах credit_request_entity и order_entity")
    public static void comparIdCreditAndOrder()  {
        var codeOrderIdWithCred ="SELECT payment_id FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var codeCreditId ="SELECT bank_id FROM credit_request_entity ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var orderData = runner.query(getConnect(), codeOrderIdWithCred, new ScalarHandler<>());
        var creditData = runner.query(getConnect(), codeCreditId, new ScalarHandler<>());
        assertEquals(creditData, orderData, "Credit and Order IDs are not equal");
    }

}


