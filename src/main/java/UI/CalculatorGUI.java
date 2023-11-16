package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CalculatorGUI extends Application {

	private TextField display;

	private String currentInput = "";
	private String operator = "";
	private double firstOperand = 0.0;
	private int openParenthesisCount = 0;
	private Button btnCloseParenthesis;
	private Button btnPeriod;
	private int periodCount = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Calculator");

		// Create the display
		display = new TextField();
		display.setEditable(false);
		display.getStyleClass().add("display-box");
//		display.setPrefHeight(50);
		display.setMinHeight(50);
		display.setMinWidth(200);
		display.setPrefWidth(Double.MAX_VALUE);

		// Create number buttons
		Button[] numberButtons = new Button[10];
		for (int i = 0; i < 10; i++) {
			numberButtons[i] = new Button(Integer.toString(i));
			numberButtons[i].getStyleClass().add("button-number");
			int finalI = i;
			numberButtons[i].setOnAction(e -> handleNumberButtonClick(finalI));
		}
//		Create Period Button
		btnPeriod = new Button(".");
		btnPeriod.getStyleClass().add("button-number");
		btnPeriod.setOnAction(e -> handleCommaClick('.'));

		// Create operator buttons
		Button btnAdd = createOperatorButton("+");
		Button btnSubtract = createOperatorButton("-");
		Button btnMultiply = createOperatorButton("X");
		Button btnDivide = createOperatorButton("/");
		Button btnEquals = new Button("=");
		btnEquals.getStyleClass().add("button-equal");
//		btnEquals.setOnAction(e -> calculateResult());
		Button btnSqrt = createOperatorButton("^");
		Button btnClear = new Button("C");
		btnClear.getStyleClass().add("button-C");
		btnClear.setOnAction(e -> clearDisplay());
		Button btnOpenParenthesis = new Button("(");
		btnOpenParenthesis.getStyleClass().add("button-operator");
		btnOpenParenthesis.setOnAction(e -> handleParenthesisClick('('));
		btnCloseParenthesis = new Button(")");
		btnCloseParenthesis.getStyleClass().add("button-operator");
		btnCloseParenthesis.setDisable(true);
		btnCloseParenthesis.setOnAction(e -> handleParenthesisClick(')'));

		// Create layout
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid-pane");
		grid.setHgap(10);
		grid.setVgap(10);

		// Add components to the layout
		grid.add(btnClear, 0, 1);
		grid.add(btnOpenParenthesis, 1, 1);
		grid.add(btnCloseParenthesis, 2, 1);
		grid.add(btnDivide, 3, 1);

		for (int i = 8; i <= 10; i++) {
			grid.add(numberButtons[i - 1], i - 8, 2);
		}
		grid.add(btnMultiply, 3, 2);

		for (int i = 5; i <= 7; i++) {
			grid.add(numberButtons[i - 1], i - 5, 3);
		}
		grid.add(btnAdd, 3, 3);

		for (int i = 2; i <= 4; i++) {
			grid.add(numberButtons[i - 1], i - 2, 4);
		}
		grid.add(btnSubtract, 3, 4);

		// '0' button spanning 2 columns
		grid.add(numberButtons[0], 0, 5, 2, 1);
		grid.add(btnPeriod, 1, 5);
		grid.add(btnSqrt, 2, 5);
		grid.add(btnEquals, 3, 5);

		grid.add(display, 0, 0, 4, 1); // display box for result

		// Set up the scene
		Scene scene = new Scene(grid, 240, 370);
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		primaryStage.setScene(scene);

		// Show the stage
		primaryStage.show();
	}

	private void clearDisplay() {
		currentInput = "";
		openParenthesisCount = 0;
		display.clear();
		btnCloseParenthesis.setDisable(true);
	}

	private Button createOperatorButton(String text) {
		Button button = new Button(text);
		button.getStyleClass().add("button-operator");
//		button.setPadding(new Insets(5));
		button.setOnAction(e -> handleOperatorButtonClick(text));
		return button;
	}

	private void handleCommaClick(char par) {
		// Check if the current input is empty or starts with an operator
		if (currentInput.isEmpty()) {
			currentInput += "0.";
		}
		// Check if the current input ends with a number or an open parenthesis
		else if (currentInput.matches(".*[0-9(]$")) {
			currentInput += ".";
		}
		// Check if the current input starts with a number
		else if (currentInput.matches("^\\s?\\d+.*")) {
			// Check if the last number already contains a decimal point
			String[] parts = currentInput.split("\\s?[+\\-*/()]\\s?");
			if (!parts[parts.length - 1].contains(".")) {
				currentInput += ".";
			}
		}

		display.setText(currentInput);
	}

	private void handleParenthesisClick(char par) {
		// Check if the current input ends with a decimal point
		if (currentInput.endsWith(".")) {
			// Do not allow parentheses to be added after a decimal point
			return;
		}
//		Checks if there is a pair for open parenthesis, if there is none, the btnCloseParenthesis will be disabled
		if (par == '(') {
			openParenthesisCount++;
		} else if (par == ')' && openParenthesisCount > 0) {
			openParenthesisCount--;
		} else {
			// Invalid ")" without a matching "("
			return;
		}

		currentInput += par;
		display.setText(currentInput);

		// Enable/disable ")" button based on open parenthesis count
		btnCloseParenthesis.setDisable(openParenthesisCount == 0);

	}

	private void handleNumberButtonClick(int number) {
		currentInput += Integer.toString(number);
		display.setText(currentInput);
	}

	private void handleOperatorButtonClick(String op) {
		if (!currentInput.isEmpty()) {
//			firstOperand = Double.parseDouble(currentInput);
			currentInput += " " + op + " ";
			display.setText(currentInput);
			operator = op;

		}
	}

//	private void calculateResult() {
//		if (!currentInput.isEmpty()) {
//			double secondOperand = Double.parseDouble(currentInput);
//			double result = 0.0;
//
//			switch (operator) {
//			case "+":
//				result = firstOperand + secondOperand;
//				break;
//			case "-":
//				result = firstOperand - secondOperand;
//				break;
//			case "X":
//				result = firstOperand * secondOperand;
//				break;
//			case "/":
//				if (secondOperand != 0) {
//					result = firstOperand / secondOperand;
//				} else {
//					display.setText("Error");
//					return;
//				}
//				break;
//			}
//			display.setText(Double.toString(result));
//			currentInput = "";
//		}
//	}
}