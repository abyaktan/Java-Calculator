package de.commsult.calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorImpl implements Calculator{
	@Override
	public double calculate(String expr) {
		
		expr = sanitizeInput(expr);
		expr = reformat(expr);
		

		String formula = expr;
		String result = "";

		if(expr.contains("(")) {
			formula = openParentheses(expr);
		} 
		
		// Now we have opened all of the brackets.
		
		if(!expr.contains("(")) {
			System.out.println("All brackets opened.");
		}
		
		if(!(formula.contains("+") || formula.contains("-") || formula.contains("/") || formula.contains("*"))) {
			result = formula;
			System.out.printf("Result: %s%n", result);
		} else if (formula.contains("-") && formula.indexOf('-') == 0) {
			result = formula;
			System.out.printf("Result: %s%n", result);
		} else {
			result = calculateOperation(parseEquation(formula));
			
			result = removeTrailingZerosAndDecimal(result);
			
			System.out.printf("Result: %s%n", result);

		}
		return Double.parseDouble(result);

		
	}
	
    // Check if the operator1 has higher or equal precedence than operator2
  
    
    
	/* To calculate equation within an innermost bracket (no more brackets inside),
	 * we have to:
	 * 1. Count how many operators there are
	 * 2. Sort the operators based on priority
	 * 3. Solve each operator's equation from highest priority
	 * 4. Replace equation with result
	 */	
	
    private static String removeTrailingZerosAndDecimal(String inputString) {
        double number = Double.parseDouble(inputString);
        
        // Convert the double back to a string to remove trailing zeros and decimal point
       // return String.format("%.0f", number);
        return String.format("%.2f", number).replaceAll("\\.?0*$", "");

    }
	
    private static String sanitizeInput(String userInput) {
    	// Removing whitespace
		userInput = userInput.replaceAll("\\s", "");

        // Remove parentheses around numbers
        String sanitizedExpression = userInput.replaceAll("\\((-?\\d+(\\.\\d+)?)\\)", "$1");
        
        return sanitizedExpression;
    }
	
	private static String reformat(String formula) {		
		// Regular expression to match numeric values
        String numberRegex = "\\d+(\\.\\d*|\\.\\b)?";


        // Pattern to find numeric values in the equation
        Pattern pattern = Pattern.compile(numberRegex);
        Matcher matcher = pattern.matcher(formula);

        // Format numeric values as doubles with two decimal points
        StringBuffer reformattedEquation = new StringBuffer();
        while (matcher.find()) {
            double value = Double.parseDouble(matcher.group());
            String formattedValue = String.format("%.2f",value);
            //String formattedValue = new DecimalFormat("0.00").format(value);
            matcher.appendReplacement(reformattedEquation, formattedValue);
        }
        matcher.appendTail(reformattedEquation);

        return reformattedEquation.toString();
	}
	
	private static String calculateOperation(Map<String, String> parsedEquation) {
		String symbol = parsedEquation.get("operator");
		double l = Double.parseDouble(parsedEquation.get("l"));
		double r = Double.parseDouble(parsedEquation.get("r"));
		
		if (symbol.equals("NONE")) {
			System.out.println("RETURNING L");
			return parsedEquation.get("l");
		} 
		if (symbol.equals("+")) {
			return String.valueOf(l + r);
		} else if (symbol.equals("-")) {
			return String.valueOf(l - r);
		} else if (symbol.equals("*")) {
			double res = l * r;
			BigDecimal bigDecimalValue = BigDecimal.valueOf(res);
			int decimalPlaces = 2;
			bigDecimalValue = bigDecimalValue.setScale(decimalPlaces, RoundingMode.HALF_UP);
			double resDouble = bigDecimalValue.doubleValue();
			return String.valueOf(resDouble);
		} else if (symbol.equals("/")) {
			double res = l / r;
			BigDecimal bigDecimalValue = BigDecimal.valueOf(res);
			int decimalPlaces = 2;
			bigDecimalValue = bigDecimalValue.setScale(decimalPlaces, RoundingMode.HALF_UP);
			double resDouble = bigDecimalValue.doubleValue();
			return String.valueOf(resDouble);
		} else {
			return "";
		}
	}
	

	
	private static Map<String, String> parseEquation(String equation) {
		List<Integer> operatorIndices = new ArrayList<>();
		
		System.out.printf("IN parseEquation.%nEquation: %s%n", equation);
		
		if (!(equation.contains("+") || equation.contains("-") || equation.contains("*") ||
				equation.contains("/") )) {
			
			equation = equation.replace("(", "");
			equation = equation.replace(")", "");
			
			System.out.printf("This is just a number: %s%n", equation);
			
			Map<String, String> parsedEquation = new HashMap<>();

			parsedEquation.put("l", equation);
			parsedEquation.put("r", equation);
			parsedEquation.put("operator", "NONE");
			return parsedEquation;
		} else if (equation.contains("-") && equation.indexOf("-") == 0) {
			equation = equation.replace("(", "");
			equation = equation.replace(")", "");
			
			System.out.printf("This is just a number: %s%n", equation);
			
			Map<String, String> parsedEquation = new HashMap<>();

			parsedEquation.put("l", equation);
			parsedEquation.put("r", equation);
			parsedEquation.put("operator", "NONE");
			return parsedEquation;
		}
				
		int operatorCounter = 0;
		for(int i=0;i<equation.length();i++) {
			
			char c = equation.charAt(i);
			// Find an operator
			if (c == '+' || c == '-' || c== '*' || c == '/') {
				
				// Make sure the operator isn't a negative symbol (as in -5)
				if(c == '-') {
					if(i == 0 || equation.charAt(i-1) == '+' || equation.charAt(i-1) == '-' ||
				equation.charAt(i-1) == '*' || equation.charAt(i-1) == '/') {
						continue;
					}
				} 
				
				++operatorCounter; 
				operatorIndices.add(i);	
				System.out.printf("Operator found: %s%n",equation.charAt(i));
			} 
			
		}
		
		// CASE: Only found 1 operator in the equation.
		if (operatorCounter == 1) {
			Map<String,String> parsedEquation = new HashMap<>();
			int idx = operatorIndices.get(0);
			char op = equation.charAt(idx);
			String l_str = equation.substring(0, idx);
			String r_str = equation.substring(idx+ 1);
			parsedEquation.put("l", l_str);
			parsedEquation.put("r", r_str);
			parsedEquation.put("operator", String.valueOf(op));
			return parsedEquation;
		}
		
		// CASE: There are multiple operators in the equation.
		System.out.println("Multiple operators case");
		int highPriorityCounter = 0;
		List<Integer> highPriorityIndices = new ArrayList<>();
		
		for(int i : operatorIndices) {
			if(equation.charAt(i) == '*' || equation.charAt(i) == '/') {
				highPriorityCounter++;
				highPriorityIndices.add(i);
			}
		}
		
		System.out.printf("Operators found: %d%n", operatorCounter);
		System.out.printf("High priority operators: %d%n",highPriorityCounter);
		
		
		// Going through all the operators in the equation
		char[] equationArray = equation.toCharArray();
		// equationArray will be our reference to get the operators from the operatorIndices or highPriorityIndices
				
		System.out.printf("Equation %s%n", equation);
		String solvedEquation = "";
		String currEquation = equation;

		for(int i=0; i<operatorCounter-1;i++) {
			Map<String,String> parsedEquation = new HashMap<>();
			int idx = -1;
			if(i<highPriorityCounter) {
				System.out.println("i < highprioritycounter");
				idx = highPriorityIndices.get(i);
			} else {
				idx = operatorIndices.get(i);
			}
			
			char op = equationArray[idx];
			System.out.printf("Operator %d: %s %n", i, op);
			
			
			int leftStartIdx = -1;
			int rightEndIdx = -1;
			
			System.out.printf("Operator idx: %d%n", idx);
			
				for(int j=idx-1;j>=0;j--) {
					char c = equationArray[j];
				//	System.out.printf("c: %s%n", c);
					if (j == 0) {
						leftStartIdx = j;
						break;
					} else if(c == '*' || c == '/' || c == '+' || c == '-') {
						if (c == '-' && (equationArray[j-1] == '+' || equationArray[j-1] == '-'
							|| equationArray[j-1] == '*' || equationArray[j-1] == '/')) {
							leftStartIdx = j;
						} else {
							leftStartIdx = j+1;
						}
						break;
					}
				}
				System.out.printf("Left start index: %d%n", leftStartIdx);

				
				for(int j=idx+1;j<equation.length();j++) {
					char c = equationArray[j];
				//	System.out.printf("Right c: %s%n", c);
					if(j==equation.length()-1) {
						rightEndIdx = j;
						break;
					} else if(c == '*' || c == '/' || c == '+' || (c == '-' && j!=i+1)) {
						rightEndIdx = j-1; //index of final char of right operand
						break;
					}
				}
				System.out.printf("Right end index: %d%n", rightEndIdx);
			//	System.out.println(equationArray[rightEndIdx]);

			
			
		//	String left = equation.substring(leftStartIdx,idx);
			String left = new String(equationArray, leftStartIdx, idx - leftStartIdx);
			System.out.printf("Left: %s%n", left);
		//	String right = equation.substring(idx+1,rightEndIdx);
			String right = new String(equationArray, idx+1, rightEndIdx - idx);
			System.out.printf("Right: %s%n", right);

			parsedEquation.put("operator", String.valueOf(op));
			parsedEquation.put("l", left);
			parsedEquation.put("r", right);
			
			String result = calculateOperation(parsedEquation);
			System.out.printf("Result: %s%n", result);
			char[] resultArray = result.toCharArray();
			
			currEquation = equation.substring(0,leftStartIdx) + 
						result + equation.substring(rightEndIdx+1);
			
			System.out.printf("Current equation: %s%n", currEquation);
						
			//AFTER EQUATION UPDATES, UPDATE LIST OF OPERATORS AGAIN!!!
			/*
			for (char c : tempArr) {
				
			}
			*/
			
			for (char c : equationArray) {
				System.out.printf("%s", c);
			} 
			
			//Updating the equation array
			for(int x = leftStartIdx; x <= rightEndIdx; x++) {
				equationArray[x] = ' ';
			}
		//	Arrays.fill(equationArray, leftStartIdx, rightEndIdx, ' ');
			System.out.println("\nEquation array before: ");
			for (char c : equationArray) {
				System.out.printf("%s", c);
			} 
			System.out.println("\nEquation array after: ");
			
			/*
			char[] tempArray = new char[resultArray.length + equationArray.length];
			System.arraycopy(resultArray, 0, tempArray, 0, resultArray.length);
			System.arraycopy(equationArray, resultArray.length, tempArray, rightEndIdx+1, equationArray.length-rightEndIdx);
			*/
			for(int z=result.length(), m=0;z>0;z--, m++) {
			
				if(rightEndIdx+1 >= z) {
					equationArray[rightEndIdx+1-z] = resultArray[m];
				} else {
					// Result doesn't fit
					//tempArray[rightEndIdx+1-z] = resultArray[m];
				}
				//System.out.printf("z=%d%n", z);
				//System.out.println(rightEndIdx+1-z);
			}

			
			for (char c : equationArray) {
				System.out.printf("%s", c);
			}
			System.out.println("");
		}
		
		
		for(char c : equationArray) {
			if(c != ' ') {
				solvedEquation += c;
			}
		}
		
		System.out.printf("Final parsed equation: %s%n",solvedEquation);
		
		
		// Parsing the final equation
		
		int indexOfOperator = -1;

		for(int i=0;i<solvedEquation.length();i++) {
			
			char c = solvedEquation.charAt(i);
			// Find an operator
			if (c == '+' || c == '-' || c== '*' || c == '/') {
				
				// Make sure the operator isn't a negative symbol (as in -5)
				if(c == '-') {
					if(i == 0 || solvedEquation.charAt(i-1) == '+' || solvedEquation.charAt(i-1) == '-' ||
							solvedEquation.charAt(i-1) == '*' || solvedEquation.charAt(i-1) == '/') {
						continue;
					}
				} 
				
				indexOfOperator = i;
			}
		}
		
		Map<String,String> parsedEquation = new HashMap<>();
		char op = solvedEquation.charAt(indexOfOperator);
		String l_str = solvedEquation.substring(0, indexOfOperator);
		String r_str = solvedEquation.substring(indexOfOperator+ 1);
		parsedEquation.put("l", l_str);
		parsedEquation.put("r", r_str);
		parsedEquation.put("operator", String.valueOf(op));
		
		return parsedEquation;
	}
	
	
	
	private static String openParentheses(String expr) {
		
		int openBracketCounter = 0;
		int firstOpenBracketIdx = expr.indexOf('(');
		if(firstOpenBracketIdx != -1) {
			System.out.printf("Index of 1st occurrence of (: %d%n", firstOpenBracketIdx);
			openBracketCounter += 1;
		}

		
		// Finding all opening brackets
		
		List<Integer> openingBracketIndices = new ArrayList<>();
		
		for(int i=0;i<expr.length();i++) {
			if(expr.charAt(i) == '(') {
				openingBracketIndices.add(i);
			}
		}
		
		System.out.print("Opening brackets: ");
		for(int bracket : openingBracketIndices) {
			System.out.printf("%d,", bracket);
		}
		System.out.printf("%n");
		
		
		// variable to save current innermost closing & opening bracket
		// initialize to -1 to know we haven't searched for it
		int innermostClosingBracketIdx = -1;
		int innermostOpeningBracketIdx = -1;
		
		while(expr.contains("(")) {

			firstOpenBracketIdx = openingBracketIndices.get(0);

			for(int i = firstOpenBracketIdx + 1; i < expr.length(); i++) {
				
				
			if (expr.charAt(i) == ')') {
					
					openBracketCounter -= 1; 
					innermostClosingBracketIdx = i; // We found the innermost closing bracket
					System.out.printf("Innermost closing bracket index: %d%n", innermostClosingBracketIdx);
					
					// Solving the equation in the innermost bracket

					// To do this we need to find the opening of the innermost bracket pair
					// The pair is the BIGGEST IDX of ( that is SMALLER than innermost closing bracket index 
					//
					for (int n = innermostClosingBracketIdx - 1; n >= 0; n--) {
						boolean pairExistsHere = openingBracketIndices.contains(n);
						if (pairExistsHere) {
							innermostOpeningBracketIdx = n;
							break;
						}
					}
					
					// And then replacing the characters from the bracket's opening
					// to the bracket's closing to 1 value (the result of the equation)
					
					String equation = expr.substring(innermostOpeningBracketIdx+1, innermostClosingBracketIdx);
					System.out.printf("Innermost bracket content is: %s%n", equation);
					System.out.printf("Innermost opening bracket index: %s%n", innermostOpeningBracketIdx);
				
					Map<String, String> parsedEq = parseEquation(equation);
					
					System.out.printf("Operator: %s%n", parsedEq.get("operator"));
					System.out.printf("L: %s, R: %s%n", parsedEq.get("l"), parsedEq.get("r"));
					
		
					String result = calculateOperation(parsedEq);
					
					String solvedExpr = expr.substring(0, innermostOpeningBracketIdx) +
										result +
										expr.substring(innermostClosingBracketIdx+1);
					
					expr = solvedExpr;
					System.out.printf("Updated expression is %s%n",expr);

					if(expr.contains("(")) {
						// Updating indices of open brackets
						openingBracketIndices.clear();
						for(int bracket=0;bracket<expr.length();bracket++) {
							if(expr.charAt(bracket) == '(') {
								openingBracketIndices.add(bracket);
							}
						}
					}
				} 
			}
	
			System.out.printf("Open brackets: %d%n", openBracketCounter);

		}
		return expr;
	}
	
	public static void main(String[] args) {
				
		//String expr = "3 + 4";
		//String expr = "3 + 4 + 5";
		//String expr = "3.6 + 4.41";
		//String expr = "3 + -2";
		//String expr = "6 - 4";
		//String expr = "6 - 4 - 1";
		//String expr = "4 - 6";
		//String expr = "4 - 6 - 3";
		//String expr = "3 * 8";
		//String expr = "3 * 8 * 2"; 
		//String expr = "3 * 8 * -2";
		//String expr = "12 / 3";
		//String expr = "12 / 3 / 2";
		//String expr = "12 / 3 / -2";
		//String expr = "12 / 5";
		//String expr = "4 * 10 + 3";
		//String expr = "3 + 4 * 10 ";
		//String expr = "16 + 4/2";
		//String expr = "4 / 2 + 16";
		//String expr = "16 - 4 / 2";
		//String expr = "4 / 2 - 16";
		//String expr = "( 3 + 4 ) * 10 ";
		//String expr = "( 3 + 4 ) * 10 / 5";
		//String expr = "( 3 + 4 ) * ( 10 / 5 + 1) - 11";
		//String expr = "(3 + (1 + (1 + 1 / 2) + (2 * 1 / 4 + 1))) * ( 10 / 5 + (1 / 4 + 3 / 4 ) )"; 
		//String expr = "6/2*(1+2)";
		//String expr = "4 + 6 + 9 - 3 * 20 / (4)"; // ERROR left: 9-3 (harusnya 3)
		//String expr = "10--10";
		//String expr = "1.+1";
		
		String expr = "((3+4)*5)+4/2";
		//String expr = "((3+4)*(9/2))";
		//String expr = "((3+-4)*(-9/2))";
		//String expr = "(1.+1)";
		//String expr = "( 3 + 4 ) * ( 10 / 5 + 1) - 11";
		//String expr = "6/2*(1+2)";
		//String expr = "(3.00 + (1.00+(1.00+1.00/2.00)+(2.00*1.00/4.00+1.00)))*(10.00/5.00+(1.00/4.00+3.00/4.00))";
		//String expr = "(3 + (1 + (1 + 1 / 2) + (2 * 1 / 4 + 1))) * ( 10 / 5 + (1 / 4 + 3 / 4 ) )";

		expr = sanitizeInput(expr);
		expr = reformat(expr);
		
		
		System.out.printf("Your expression is: %s%n",expr);
		System.out.printf("The length of the string is %d%n", expr.length());
		
		// Because brackets takes highest priority, the 1st step should be to find the brackets
		// 1. Finding brackets
		
		String formula = expr;

		if(expr.contains("(")) {
			formula = openParentheses(expr);
		} 
		
		// Now we have opened all of the brackets.
		
		if(!expr.contains("(")) {
			System.out.println("All brackets opened.");
		}
		
		if(!(formula.contains("+") || formula.contains("-") || formula.contains("/") || formula.contains("*"))) {
			String result = formula;
			System.out.printf("Result: %s%n", result);
		} else if (formula.contains("-") && formula.indexOf('-') == 0) {
			String result = formula;
			System.out.printf("Result: %s%n", result);
		} else {
			String result = calculateOperation(parseEquation(formula));
			
			result = removeTrailingZerosAndDecimal(result);
			
			System.out.printf("Result: %s%n", result);
		}
		
		
		
	}
	

}
