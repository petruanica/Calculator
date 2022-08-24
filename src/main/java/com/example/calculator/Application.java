package com.example.calculator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 550);
        stage.setTitle("Basic Calculator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static String parseOperation(String previous, String current) throws ArithmeticException {
        BigDecimal a = BigDecimal.valueOf(Double.parseDouble(previous.substring(0, previous.length() - 1).replaceAll(",", "")));
        BigDecimal b = BigDecimal.valueOf(Double.parseDouble(current.replaceAll(",", "")));
        char operation = previous.charAt(previous.length() - 1);
        if (b.equals(new BigDecimal(0)) && operation == '÷'){
            throw new ArithmeticException();
        }
        return switch (operation) {
            case '+' -> formatNumberDecimals(a.add(b));
            case '-' -> formatNumberDecimals(a.subtract(b));
            case '*' -> formatNumberDecimals(a.multiply(b));
            case '÷' -> formatNumberDecimals(a.divide(b, 6, RoundingMode.HALF_UP));
            default -> "null";
        };
    }

    public static String formatNumberDecimals(BigDecimal input) {
        if (input.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) == -1 || input.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 1) {
            return "overflow";
        }
        DecimalFormat df = new DecimalFormat("#.######");
        return formatNumber(df.format(input));
    }

    public static String formatNumber(String number) {
        number = number.replaceAll("," ,"");
        if (number.contains(".")) {
            String[] parts = number.split("\\.");
            String integer = parts[0];
            for (int i = integer.length() - 3; i > 0; i -= 3) {
                integer = integer.substring(0, i) + "," + integer.substring(i);
            }
            if (parts.length == 1) {
                return integer + ".";
            }
            return integer + "." + parts[1];
        } else {
            for (int i = number.length() - 3; i > 0; i -= 3) {
                number = number.substring(0, i) + "," + number.substring(i);
            }
            return number;
        }
    }

    public static boolean checkNumberSize(String input) {
        input = input.replaceAll("," ,"");
        try {
            return input.length() <= 25 && BigDecimal.valueOf(Double.parseDouble(input)).compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) != 1;
        } catch (NumberFormatException e) {
            return input.length() <= 25;
        }
    }
}