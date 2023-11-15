package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Calculator");

		// Create the display
		display = new TextField();
		display.setEditable(false);
		display.setStyle("-fx-control-inner-background: beige;");
		display.setMinHeight(50);

		// Create number buttons
		Button[] numberButtons = new Button[10];
		for (int i = 0; i < 10; i++) {
			numberButtons[i] = new Button(Integer.toString(i));
			numberButtons[i].getStyleClass().add("button-number");
			int finalI = i;
			numberButtons[i].setOnAction(e -> handleNumberButtonClick(finalI));
		}

		// Create operator buttons
		Button btnAdd = createOperatorButton("+", "-fx-background-color: #654321;", "-fx-text-fill: white;");
		Button btnSubtract = createOperatorButton("-", "-fx-background-color: #654321;", "-fx-text-fill: white;");
		Button btnMultiply = createOperatorButton("X", "-fx-background-color: #654321;", "-fx-text-fill: white;");
		Button btnDivide = createOperatorButton("/", "-fx-background-color: #654321;", "-fx-text-fill: white;");
		Button btnEquals = createOperatorButton("=", "-fx-background-color: red;", "-fx-text-fill: white;");
//        btnEquals.setOnAction(e -> calculateResult());

		Button btnClear = new Button("C");
		btnClear.setOnAction(e -> clearDisplay());
		Button btnOpenParenthesis = new Button("(");
		btnOpenParenthesis.setOnAction(e -> handleParenthesisClick('('));
		Button btnCloseParenthesis = new Button(")");
		btnCloseParenthesis.setOnAction(e -> handleParenthesisClick(')'));

		// Create layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		// Add components to the layout
		grid.add(display, 0, 0, 5, 1);

		for (int i = 1; i <= 9; i++) {
			grid.add(numberButtons[i], (i - 1) % 3, 3 - (i - 1) / 3);
		}
		grid.add(numberButtons[0], 1, 4);

		grid.add(btnAdd, 4, 1);
		grid.add(btnSubtract, 4, 2);
		grid.add(btnMultiply, 3, 1);
		grid.add(btnDivide, 3, 2);
		grid.add(btnEquals, 4, 4);

		grid.add(btnClear, 3, 4);
		grid.add(btnOpenParenthesis, 3, 3);
		grid.add(btnCloseParenthesis, 4, 3);
		// Set up the scene
		Scene scene = new Scene(grid, 300, 400);
//		scene.getStylesheets().add(getClass().getResource("/UI/style.css").toExternalForm());
		primaryStage.setScene(scene);

		// Show the stage
		primaryStage.show();
	}

	private void clearDisplay() {
		currentInput = "";
		display.clear();
	}

	private Button createOperatorButton(String text, String backgroundStyle, String textFillStyle) {
		Button button = new Button(text);
//		button.getStyleClass().add("button-operator");
//		button.setMinSize(10, 10); // Set a fixed size for the button
//		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow the button to grow
//		button.setPadding(new Insets(5));
		button.setOnAction(e -> handleOperatorButtonClick(text));
		return button;
	}

	private void handleParenthesisClick(char par) {
		currentInput += par;
		display.setText(currentInput);
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
