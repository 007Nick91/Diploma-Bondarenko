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
   public static String getGenMonth() {
       return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));

    }

    public static String generateYear() {
        var year = new String[]{"23","24","25","26","27","28"};
        return year[new Random().nextInt(year.length)];
    }


    public static String generateOwner() {
        var faker = new Faker(new Locale("eng"));
        return faker.name().lastName() +" " + faker.name().firstName();
    }
    public static int getGenCvc() {
        int min = 100;
        int max = 999;
        return min + (int)(Math.random()*max);
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
