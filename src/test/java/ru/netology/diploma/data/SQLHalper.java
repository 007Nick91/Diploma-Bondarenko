package ru.netology.diploma.data;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLHalper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHalper() {
    }

    private static Connection getConnect() throws SQLException {
        String url = System.getProperty("db.url");
        String user = System.getProperty("db.user");
        String password = System.getProperty("db.password");

        Connection connect = DriverManager.getConnection(url, user, password);
        return connect;
    }


    //Проверка наличия записи в базе о покупке с выводом статуса карты
    @SneakyThrows
    public static String verifyOrderPayment() {
        var codeSQL =
                "SELECT payment_entity.status FROM payment_entity " +
                        "JOIN order_entity on payment_entity.transaction_id = order_entity.payment_id " +
                        "ORDER BY payment_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return code;
    }

    //Проверка наличия записи в базе о покупке в кредит с выводом статуса карты
    @SneakyThrows
    public static String verifyOrderCredit() {
        var codeSQL =
                "SELECT credit_request_entity.status FROM credit_request_entity " +
                        "JOIN order_entity on credit_request_entity.bank_id = order_entity.payment_id " +
                        "ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return code;
    }

    ////    Проверка наличия записей в таблицах
    @SneakyThrows
    @Step("Проверка наличия записей в таблице payment_entity")
    public static String verifyPaymentNotNull() {
        var codeChekLinePayment = "SELECT * FROM payment_entity ORDER BY payment_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var paymentCode = runner.query(conn, codeChekLinePayment, new ScalarHandler<String>());
        return paymentCode;
    }

    @SneakyThrows
    @Step("Проверка наличия записей в таблице credit_request_entity")
    public static String verifyCreditNotNull() {
        var codeChekLineCredit = "SELECT * FROM credit_request_entity ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var creditCode = runner.query(conn, codeChekLineCredit, new ScalarHandler<String>());
        return creditCode;
    }

    @SneakyThrows
    @Step("Проверка наличия записей в таблице order_entity")
    public static String verifyOrderNotNull() {
        var codeChekLineOrder = "SELECT * FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var orderCode = runner.query(conn, codeChekLineOrder, new ScalarHandler<String>());
        return orderCode;

    }

    ////    Сравнение внешних ключей в таблицах
    @SneakyThrows
    @Step("Внешний ключ в таблице payment_entity")
    public static String comparIdPayment() {
        var codePaymentId = "SELECT transaction_id FROM payment_entity ORDER BY payment_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var paymentData = runner.query(conn, codePaymentId, new ScalarHandler<String>());
        return paymentData;

    }

    @SneakyThrows
    @Step("Внешний ключ в таблице order_entity")
    public static String comparIdOrder() {
        var codeOrderIdWithPay = "SELECT payment_id FROM order_entity ORDER BY order_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var orderData = runner.query(conn, codeOrderIdWithPay, new ScalarHandler<String>());
        return orderData;

    }

    @SneakyThrows
    @Step("Сравнение внешних ключей в таблицах credit_request_entity и order_entity")
    public static String comparIdCredit() {
        var codeCreditId = "SELECT bank_id FROM credit_request_entity ORDER BY credit_request_entity.created DESC LIMIT 1;";
        var conn = getConnect();
        var creditData = runner.query(conn, codeCreditId, new ScalarHandler<String>());
        return creditData;

    }

}


