package ru.netology.diploma.data;

public class DataCards {
    private static String month = "12";
    private static String year = "23";
    private static String owner = "Petr Petrov";
    private static String cvc = "888";
    private static String approvedCard = "4444444444444441";
    private static String declinedCard = "4444444444444442";

    public static String getMonth() {
        return month;
    }

    public static String getYear() {
        return year;
    }

    public static String getOwner() {
        return owner;
    }

    public static String getCvc() {
        return cvc;
    }

    public static String getApprovedCardNumber() {
        return approvedCard;
    }

    public static String getDeclinedCardNumber() {
        return declinedCard;
    }
}
