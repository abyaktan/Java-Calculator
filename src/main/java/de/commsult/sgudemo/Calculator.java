package de.commsult.sgudemo;

public class Calculator {

    public static void main(String[] args) {

        Calculator calc = new Calculator();

        double result = 0;
        try {
            result = calc.divide(10, 10);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("there occured an error: " + e.getMessage());
        }

    }

    public int add(int a, int b) {
        return a + b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("you can not divide by zero!");
        }

        return a / b;
    }
}
