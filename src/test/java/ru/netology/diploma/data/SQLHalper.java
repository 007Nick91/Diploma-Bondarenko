package ru.netology.diploma.data;

public class SQLHalper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHalper(){
    }
    private static Connection getConnect() throws SQLException{
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }
    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode(){
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var conn = getConnect();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return new DataHelper.VerificationCode(code);
    }
    @SneakyThrows
    public static void cleanDatabase(){
        var connection = getConnect();
        runner.execute(connection,"DELETE FROM auth_codes");
        runner.execute(connection,"DELETE FROM card_transactions");
        runner.execute(connection,"DELETE FROM cards");
        runner.execute(connection,"DELETE FROM users");
    }

    @SneakyThrows
    public static void cleanAuthCodes(){
        var connection = getConnect();
        runner.execute(connection,"DELETE FROM auth_codes");
    }
}

    private static String verificationOrderByPaymentStatus = "SELECT payment_entity.status FROM payment_entity " +
            "JOIN order_entity on payment_entity.transaction_id = order_entity.payment_id " +
            "WHERE payment_entity.created IN (SELECT max(payment_entity.created) FROM payment_entity);";

    private static String verificationOrderByCreditStatus = "SELECT credit_request_entity.status " +
            "FROM credit_request_entity JOIN order_entity " +
            "on credit_request_entity.bank_id = order_entity.payment_id " +
            "WHERE credit_request_entity.created IN (SELECT max(credit_request_entity.created) " +
            "FROM credit_request_entity);"

    private static String checkRowOfCredit = "SELECT * FROM credit_request_entity WHERE created IN (SELECT max(created) " +
            "FROM credit_request_entity);";
    private static String checkRowOfOrder = "SELECT * FROM order_entity WHERE created IN (SELECT max(created) " +
            "FROM order_entity);";
    private static String checkRowOfPayment = "SELECT * FROM payment_entity WHERE created IN (SELECT max(created) " +
            "FROM payment_entity);";
