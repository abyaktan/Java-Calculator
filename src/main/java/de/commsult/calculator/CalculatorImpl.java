package de.commsult.calculator;

import java.util.Stack;

public class CalculatorImpl implements Calculator {

    @Override
    public double calculate(String formula) {
        return evaluateFormula(formula);
    }

    private double evaluateFormula(String formula) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (char ch : formula.toCharArray()) {
            if (ch == ' ') {
                continue; // Skip spaces
            }

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);

                // Collect the entire number, including decimal points
                while ((ch = getNextChar(formula)) != 0 && (Character.isDigit(ch) || ch == '.')) {
                    numBuilder.append(ch);
                }

                numbers.push(Double.parseDouble(numBuilder.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    applyOperator(numbers, operators.pop());
                }
                operators.pop(); // Pop the '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    applyOperator(numbers, operators.pop());
                }
                operators.push(ch);
            }
        }
        
        while (!operators.isEmpty()) {
            applyOperator(numbers, operators.pop());
        }

        return numbers.pop();
    }

    private void applyOperator(Stack<Double> numbers, char operator) {
        double operand2 = numbers.pop();
        double operand1 = numbers.pop();

        switch (operator) {
            case '+':
                numbers.push(operand1 + operand2);
                break;
            case '-':
                numbers.push(operand1 - operand2);
                break;
            case '*':
                numbers.push(operand1 * operand2);
                break;
            case '/':
                numbers.push(operand1 / operand2);
                break;
        }
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private char getNextChar(String formula) {
        return formula.isEmpty() ? 0 : formula.charAt(0);
    }

}
