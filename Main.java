package com.example.demo;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите любое выражение из предложенных:[\"a\" + \"b\", \"a\" - \"b\", \"a\" * x, \"a\" / x], где a и b - это любые буквы и символы в кавычках \nx - это любое натуральное число от 1 до 10 \nДля выхода введите exit \nМежду знаком операции обязателен с двух сторон пробел");
        //Выход из программы
        final String EXIT_STRING = "exit";
        while (true) {
            String string = scanner.nextLine();
            if (string.equals(EXIT_STRING))
                return;
            //Вывод результата на консоль
            String result = Calculator.calculate(string);
            System.out.println(result);
        }
    }
    //Перечисление операторов для работы с методами
    enum Operation {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE
    }

    static class Parser {
        private String leftOperand; // Строка слева от оператора
        private Operation operation; // Оператор выражения
        private String rightOperandString; // Строка справа от оператора
        private int rightOperandNumber; // Число справа от оператора
        private boolean isRightOperandString; // Сравнение для определения правого значения от оператора: строка или число

        // Метод для элементов выражения
        public Parser(String stringToParse) {
            // Разделитель между переменными и оператором
            String delimiter = " ";
            String[] splitString = stringToParse.split(delimiter);

            //Задаёт количество вводимых элементов на консоль
            final int NUMBER_OF_ELEMENTS = 3;
            if (splitString.length != NUMBER_OF_ELEMENTS)
                throw new IllegalArgumentException("Строка должна содержать 3 элемента");
            // Индекс для переменных выражения
            leftOperand = parseStringOperand(splitString[0]);
            operation = parseOperation(splitString[1]);
            parseRightOperand(splitString[2]);
        }

        public String getLeftOperand() {
            return leftOperand;
        }

        public Operation getOperation() {
            return operation;
        }

        public String getRightOperandString() {
            if (!isRightOperandString)
                throw new IllegalStateException("Правый операнд не является строкой");

            return rightOperandString;
        }

        public int getRightOperandNumber() {
            if (isRightOperandString)
                throw new IllegalStateException("Правый операнд не является числом");

            return rightOperandNumber;
        }

        public boolean isRightOperandString() {
            return isRightOperandString;
        }

        // Метод для определения неизвестных символов (выдаёт ошибку если строки не в "")
        private String parseStringOperand(String string) {
            if (string.charAt(0) != '"' || string.charAt(string.length() - 1) != '"')
                throw new IllegalArgumentException("Неизвестные символы");

            String result = string.substring(1, string.length() - 1);
            // Задаёт максимальную длину строки
            final int MAX_LENGTH = 10;
            if (result.length() > MAX_LENGTH)
                throw new IllegalArgumentException("Превышено максимальное значение длины строки");

            return result;
        }

        // Лямбла выражения (заданные значения в кейсах переходят в операции)
        private Operation parseOperation(String string) {
            return switch (string) {
                case "+" -> Operation.PLUS;
                case "-" -> Operation.MINUS;
                case "/" -> Operation.DIVIDE;
                case "*" -> Operation.MULTIPLY;
                // Если введено иное значение оператора
                default -> throw new IllegalArgumentException("Неизвестный оператор");
            };
        }

        // Метод для определения правого от оператора значения: строка или число
        private void parseRightOperand(String string) {
            if (string.charAt(0) == '"') {
                isRightOperandString = true;
                rightOperandString = parseStringOperand(string);
            }
            else {
                isRightOperandString = false;
                rightOperandNumber = parseNumberOperand(string);
            }
        }

        // Задаёт диапазон для числа
        private int parseNumberOperand(String string) {
            final int MIN_VALUE = 1;
            final int MAX_VALUE = 10;

            int numberOperand = Integer.parseInt(string);

            if (numberOperand < MIN_VALUE || numberOperand > MAX_VALUE) {
                String errorMessage = String.format("Число должно быть между %d и %d", MIN_VALUE, MAX_VALUE);
                throw new IllegalArgumentException(errorMessage);
            }

            return numberOperand;
        }
    }
    static class Calculator {
        // Метод для операций
        public static String calculate(String string) {
            Parser parser = new Parser(string);

            // Лямбда выражения (заданные операторы в кейсах переходят в методы)
            String result =  switch (parser.getOperation()) {
                case PLUS -> plus(parser);
                case MINUS -> minus(parser);
                case DIVIDE -> divide(parser);
                case MULTIPLY -> multiply(parser);
                default -> throw new IllegalArgumentException("Неподдерживаемая операция");
            };

            return "\"" + result + "\"";
        }

        //Метод для выполнение сложения (только строка и строка)
        private static String plus(Parser parser) {
            return parser.getLeftOperand() + parser.getRightOperandString();
        }

        //Метод для выполнение вычитания (только строка и строка)
        private static String minus(Parser parser) {
            String leftOperand = parser.getLeftOperand();
            String rightOperand = parser.getRightOperandString();
            // Записывает в ответ "" если в неё нет вхождения изначальной строки
            return leftOperand.replaceAll(rightOperand, "");
        }

        //Метод для выполнение деления (только строка и число)
        private static String divide(Parser parser) {
            String leftOperand = parser.getLeftOperand();
            int rightOperand = parser.getRightOperandNumber();
            return leftOperand.substring(0, leftOperand.length() / rightOperand);
        }

        //Метод для выполнение умножения (только строка и число)
        private static String multiply(Parser parser) {
            String leftOperand = parser.getLeftOperand();
            int rightOperand = parser.getRightOperandNumber();
            String result = leftOperand.repeat(rightOperand);
            //Считывает длину строки результата и ограничиванием при превышении значнения
            final int MAX_LENGTH = 40;
            if (result.length() > MAX_LENGTH)
                result = result.substring(0, MAX_LENGTH) + "...";

            return result;
        }
    }
}
