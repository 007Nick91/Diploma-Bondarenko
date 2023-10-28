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

    public static String generateIncorrectNumber() {
        var data = new String[]{"-1", "13", "1", " "};
        return data[new Random().nextInt(data.length)];
    }

    public static String generateIncorrectName() {
        var inName = new String[]{"P", "Петр Петров", "Petr", "%$", "qw", "йц"};
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
