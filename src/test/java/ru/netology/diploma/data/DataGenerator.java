package ru.netology.diploma.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {


    private DataGenerator() {

    }

    private static String approvedCard = "4444444444444441";
    private static String declinedCard = "4444444444444442";
    private static String notRegistredCard = "4444444444444444";

    //Генераторы месяца
    public static String generateMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));

    }

    public static String generateYear() {
        int index = new Random().nextInt(5) + 1;
        String year = LocalDate.now().plusYears(index).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    public static String generateOwner() {
        var faker = new Faker(new Locale("eng"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static int generateCvc() {
        int min = 100;
        int max = 999;
        return min + (int) (Math.random() * max);
    }
// Генераторы некорректных данных
    public static String generateIncorrectNumber() {
    var data = new String[]{"0", "1", "13", "29", "99"};
    return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectZero() {
        var data = new String[]{ "000", "0000", "0000"};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectNegative() {
        var data = new String[]{"-1", "-5", "-13", "-99"};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectSpace() {
        var data = new String[]{" ", "  ", "   "};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectletters() {
        var data = new String[]{"q", "qw","qwe","qwer"};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectSymbols() {
        var data = new String[]{"@", "@#", "@#$","@#$%"};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectRus() {
        var data = new String[]{"Петр Петров", "Иван Иванов","Оля Ольгина","Аля Ким"};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectName() {
        var inName = new String[]{"Ivan","Olya","Petr","Alya","Oleg"};
        return inName[new Random().nextInt(inName.length)];
    }


    public static String getApprovedCard() {
        return approvedCard;
    }

    public static String getDeclinedCard() {
        return declinedCard;
    }

    public static String getNotRegistredCard() {
        return notRegistredCard;
    }


}
