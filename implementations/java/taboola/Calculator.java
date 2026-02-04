package taboola;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Calculator {
    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }

    public double increment(double a) {
        return a + 1;
    }

    public double decrement(double a) {
        return a - 1;
    }

    HashMap<String, Var> variables = new HashMap<>();

    public void setVariable(String name, double value) {
        variables.put(name, new Var(name, value));
    }

    /**
     * Processes tokens by handling increment/decrement operators and replacing variables.
     * Modifies the tokens array in place.
     *
     * @param tokens Array of tokens to process
     */
    private void processTokens(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].startsWith("++") || tokens[i].startsWith("--")) {
                // Pre-increment/decrement: ++x, --x
                tokens[i] = replacePreIncrementDecrement(tokens[i]);
            } else if (tokens[i].endsWith("++") || tokens[i].endsWith("--")) {
                // Post-increment/decrement: x++, x--
                tokens[i] = replacePostIncrementDecrement(tokens[i]);
            } else if (variables.containsKey(tokens[i])) {
                // Replace variable with its current value
                tokens[i] = String.valueOf(variables.get(tokens[i]).getValue());
            }
        }
    }

    /**
     * End-to-end method to evaluate a mathematical expression with variables,
     * increment/decrement operators, parentheses, and operator precedence.
     * Also supports assignment expressions like "a = x + 8 + c * 3"
     *
     * @param expression The expression to evaluate (tokens must be space-separated)
     * @return The evaluated result
     *
     * Examples:
     * - "x++ + ++b * ( d - 8 ) + x" where x=5, b=2, d=3
     * - "a = x + 8 + c * 3" where x=5, c=2 (assigns result to variable 'a')
     */
    public double evaluate(String expression) {
        // Check if this is an assignment expression (simple or compound)
        if (expression.contains(" = ") || expression.contains(" += ") ||
            expression.contains(" -= ") || expression.contains(" *= ") ||
            expression.contains(" /= ") || expression.contains(" %= ")) {
            return evaluateAssignment(expression);
        }

        // Step 1: Tokenize the expression
        String[] tokens = expression.split(" ");

        // Step 2: Process increment/decrement operators and replace variables
        processTokens(tokens);

        // Step 3: Reconstruct the expression with replaced values
        String processedExpression = String.join(" ", tokens);

        // Step 4: Handle parentheses (evaluates innermost expressions first)
        String withoutBrackets = openBrackets(processedExpression);

        // Step 5: Evaluate the final expression with operator precedence
        String result = calculateCleanExpression(withoutBrackets).trim();

        // Step 6: Parse and return the final result
        // Filter out any empty strings and get the final numeric value
        String[] resultTokens = Arrays.stream(result.split(" "))
                                      .filter(s -> !s.isEmpty())
                                      .toArray(String[]::new);

        return Double.parseDouble(resultTokens[0]);
    }

    /**
     * Evaluates an assignment expression and stores the result in a variable.
     * Supports compound assignments like: a += expr, a -= expr, a *= expr, a /= expr, a %= expr
     *
     * @param expression Assignment expression (e.g., "a = x + 8 + c * 3")
     * @return The evaluated and assigned value
     */
    private double evaluateAssignment(String expression) {
        String varName;
        String rightExpression;
        String operator = null;

        // Check for compound assignment operators first
        if (expression.contains(" += ")) {
            String[] parts = expression.split(" \\+= ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
            operator = "+";
        } else if (expression.contains(" -= ")) {
            String[] parts = expression.split(" -= ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
            operator = "-";
        } else if (expression.contains(" *= ")) {
            String[] parts = expression.split(" \\*= ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
            operator = "*";
        } else if (expression.contains(" /= ")) {
            String[] parts = expression.split(" /= ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
            operator = "/";
        } else if (expression.contains(" %= ")) {
            String[] parts = expression.split(" %= ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
            operator = "%";
        } else {
            // Simple assignment
            String[] parts = expression.split(" = ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment expression: " + expression);
            }
            varName = parts[0].trim();
            rightExpression = parts[1].trim();
        }

        // Evaluate the right side expression
        double value = evaluate(rightExpression);

        // Handle compound assignments
        if (operator != null) {
            if (!variables.containsKey(varName)) {
                throw new IllegalArgumentException("Variable not defined: " + varName);
            }
            double currentValue = variables.get(varName).getValue();
            value = calculate(currentValue, value, operator);
        }

        // Store the result in the variable
        setVariable(varName, value);

        return value;
    }


    public String replacePreIncrementDecrement(String token) {
        String operator = token.substring(0, 2);
        String varName = token.substring(2).trim();
        if (!variables.containsKey(varName)) {
            throw new IllegalArgumentException("Variable not defined: " + varName);
        }
        if (operator.equals("++")) {
            double newValue = increment(variables.get(varName).getValue());
            variables.get(varName).setValue(newValue);
            return String.valueOf(newValue);
        } else if (operator.equals("--")) {
            double newValue = decrement(variables.get(varName).getValue());
            variables.get(varName).setValue(newValue);
            return String.valueOf(newValue);
        } else {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public String replacePostIncrementDecrement(String token) {
        String varName = token.substring(0, token.length() - 2).trim();
        String operator = token.substring(token.length() - 2);
        if (!variables.containsKey(varName)) {
            throw new IllegalArgumentException("Variable not defined: " + varName);
        }
        double currentValue = variables.get(varName).getValue();
        if (operator.equals("++")) {
            double newValue = increment(currentValue);
            variables.get(varName).setValue(newValue);
            return String.valueOf(currentValue);
        } else if (operator.equals("--")) {
            double newValue = decrement(currentValue);
            variables.get(varName).setValue(newValue);
            return String.valueOf(currentValue);
        } else {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }


    public String calculateCleanExpression(String expression) {
        String[] tokens = expression.split(" ");
        if(tokens.length==1){
            return tokens[0];
        }
        for (int i = 0; i < tokens.length; i++) {
            if ((Arrays.asList("*", "/", "%").contains(tokens[i]))) {
                tokens[i + 1] = String.valueOf(calculate(Double.parseDouble(tokens[i - 1]), Double.parseDouble(tokens[i + 1]), tokens[i]));
                tokens[i] = "";
                tokens[i - 1] = "";
                i++;
            }
        }

        String b = String.join(" ", tokens).trim();
        System.out.println(b);
        String[] tokens2 = Arrays.stream(b.split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
        for (int i = 0; i < tokens2.length; i++) {
            if ((Arrays.asList("+", "-").contains(tokens2[i]))) {
                tokens2[i + 1] = String.valueOf(calculate(Double.parseDouble(tokens2[i - 1]), Double.parseDouble(tokens2[i + 1]), tokens2[i]));
                tokens2[i] = "";
                tokens2[i - 1] = "";
                i++;
            }
        }
        return String.join(" ", tokens2);
    }

    private Double calculate(double v, double v1, String operator) {
        switch (operator) {
            case "*":
                return v * v1;
            case "/":
                return v / v1;
            case "%":
                return v % v1;
            case "+":
                return v + v1;
            case "-":
                return v - v1;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

    }

    public String openBrackets(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].startsWith("(")) {
                stack.add(i);
            }
            if (tokens[i].endsWith(")")) {
                int openIndex = stack.pop();
                String subExpression = String.join(" ", getElements(tokens, i, openIndex)).trim();
                String result = calculateCleanExpression(subExpression).trim();
                tokens[openIndex] = result;
                for(int k = openIndex + 1; k <= i; k++) {
                    tokens[k] = "";
                }
            }
        }

        if (!stack.empty()) {
            throw new IllegalArgumentException("Mismatched parentheses in expression.");
        }
        String[] array = Arrays.stream(tokens).filter(s -> !s.isEmpty()).toArray(String[]::new);

        return String.join(" ", array).trim();
    }

    private static String[] getElements(String[] tokens, int i, int openIndex) {
        return Arrays.stream(Arrays.copyOfRange(tokens, openIndex + 1, i)).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        // Demo: User's requested feature
        System.out.println("=== Demo: Assignment Expression ===");
        calculator.setVariable("x", 5);
        calculator.setVariable("c", 2);
        System.out.println("Initial: x=5, c=2");
        calculator.evaluate("a = x + 8 + c * 3");
        System.out.println("After: a = x + 8 + c * 3");
        System.out.println("Value of a: " + calculator.variables.get("a").getValue());
        System.out.println();

        // Set up variables
        calculator.setVariable("x", 5);
        calculator.setVariable("b", 2);
        calculator.setVariable("d", 3);

        // Test 1: End-to-end evaluation with variables and operators
        System.out.println("=== Test 1: Complex expression with variables ===");
        String expression1 = "x++ + ++b * ( d - 8 ) + x";
        System.out.println("Expression: " + expression1);
        System.out.println("Initial values: x=5, b=2, d=3");
        double result1 = calculator.evaluate(expression1);
        System.out.println("Result: " + result1);
        System.out.println("Final values: x=" + calculator.variables.get("x").getValue() +
                           ", b=" + calculator.variables.get("b").getValue() +
                           ", d=" + calculator.variables.get("d").getValue());
        System.out.println();

        // Reset variables for test 2
        calculator.setVariable("x", 5);
        calculator.setVariable("b", 2);
        calculator.setVariable("d", 3);

        // Test 2: Expression with only numbers
        System.out.println("=== Test 2: Expression with numbers only ===");
        String expression2 = "1 * 8 - 7 + 6 / 2 + 3 % 2";
        System.out.println("Expression: " + expression2);
        double result2 = calculator.evaluate(expression2);
        System.out.println("Result: " + result2);
        System.out.println();

        // Test 3: Nested parentheses
        System.out.println("=== Test 3: Nested parentheses ===");
        String expression3 = "( 1 * ( ( 8 - 7 ) ) + 6 / ( 2 + 3 ) % 2 )";
        System.out.println("Expression: " + expression3);
        double result3 = calculator.evaluate(expression3);
        System.out.println("Result: " + result3);
        System.out.println();

        // Test 4: Simple expression
        System.out.println("=== Test 4: Simple arithmetic ===");
        String expression4 = "10 + 5 * 2";
        System.out.println("Expression: " + expression4);
        double result4 = calculator.evaluate(expression4);
        System.out.println("Result: " + result4);
        System.out.println();

        // Test 5: Assignment expression
        System.out.println("=== Test 5: Assignment expression ===");
        calculator.setVariable("x", 5);
        calculator.setVariable("c", 2);
        System.out.println("Initial values: x=5, c=2");
        String expression5 = "a = x + 8 + c * 3";
        System.out.println("Expression: " + expression5);
        double result5 = calculator.evaluate(expression5);
        System.out.println("Result: a = " + result5);
        System.out.println("Variable 'a' value: " + calculator.variables.get("a").getValue());
        System.out.println();

        // Test 6: Compound assignment operators
        System.out.println("=== Test 6: Compound assignment (+=, -=, *=, /=, %=) ===");
        calculator.setVariable("num", 10);
        System.out.println("Initial: num = 10");

        double result6a = calculator.evaluate("num += 5");
        System.out.println("num += 5  =>  num = " + result6a);

        double result6b = calculator.evaluate("num -= 3");
        System.out.println("num -= 3  =>  num = " + result6b);

        double result6c = calculator.evaluate("num *= 2");
        System.out.println("num *= 2  =>  num = " + result6c);

        double result6d = calculator.evaluate("num /= 4");
        System.out.println("num /= 4  =>  num = " + result6d);

        double result6e = calculator.evaluate("num %= 5");
        System.out.println("num %= 5  =>  num = " + result6e);
        System.out.println();

        // Test 7: Complex assignment with variables and operators
        System.out.println("=== Test 7: Complex assignment ===");
        calculator.setVariable("x", 10);
        calculator.setVariable("y", 3);
        System.out.println("Initial: x=10, y=3");
        String expression7 = "result = x * 2 + y - 5";
        System.out.println("Expression: " + expression7);
        double result7 = calculator.evaluate(expression7);
        System.out.println("Result: result = " + result7);
        System.out.println();

        // Test 8: Assignment with parentheses
        System.out.println("=== Test 8: Assignment with parentheses ===");
        calculator.setVariable("p", 4);
        calculator.setVariable("q", 6);
        System.out.println("Initial: p=4, q=6");
        String expression8 = "answer = ( p + q ) * 2";
        System.out.println("Expression: " + expression8);
        double result8 = calculator.evaluate(expression8);
        System.out.println("Result: answer = " + result8);
    }
}