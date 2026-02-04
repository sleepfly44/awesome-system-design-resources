package taboola;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator {


    // Package-private for testing
    double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }

    double increment(double a) {
        return a + 1;
    }

    double decrement(double a) {
        return a - 1;
    }

    HashMap<String, Var> variables = new HashMap<>();

    public void setVariable(String name, double value) {
        variables.put(name, new Var(name, value));
    }

    /**
     * Normalizes an expression by adding spaces around operators and parentheses.
     *
     * @param expression The expression to normalize
     * @return Normalized expression with spaces
     */
    private String normalizeExpression(String expression) {
        // Add spaces around parentheses
        expression = expression.replace("(", " ( ").replace(")", " ) ");
        // Clean up multiple spaces
        expression = expression.replaceAll("\\s+", " ").trim();
        return expression;
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
     * Processes a series of assignment expressions and returns the final variable values.
     * Supports a subset of Java numeric expressions and operators.
     *
     * Supported operators: +, -, *, /, %, ++, --, +=, -=, *=, /=, %=
     * Supported features: variables, parentheses, operator precedence
     *
     * @param expressions Array of assignment expressions
     * @return HashMap of variable names to their Var objects
     */
    public HashMap<String, Var> processExpressions(String[] expressions) {
        for (String expression : expressions) {
            expression = expression.trim();
            if (!expression.isEmpty()) {
                evaluate(expression);
            }
        }
        return variables;
    }

    /**
     * Formats the variable results in the required output format.
     * Format: (var1=value1,var2=value2,...)
     *
     * @param results HashMap of variable names to Var objects
     * @return Formatted string
     */
    public String formatOutput(HashMap<String, Var> results) {
        if (results.isEmpty()) {
            return "()";
        }

        StringBuilder sb = new StringBuilder("(");
        results.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                if (sb.length() > 1) {
                    sb.append(",");
                }
                double value = entry.getValue().getValue();
                // Display as integer if it's a whole number
                if (value == Math.floor(value) && !Double.isInfinite(value)) {
                    sb.append(entry.getKey()).append("=").append((int)value);
                } else {
                    sb.append(entry.getKey()).append("=").append(value);
                }
            });
        sb.append(")");
        return sb.toString();
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
        // Normalize the expression (add spaces around parentheses and operators)
        expression = normalizeExpression(expression);

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
        String result = calculateCleanExpression(withoutBrackets);

        // Step 6: Parse and return the final result
        // calculateCleanExpression returns a single numeric value as a string
        return Double.parseDouble(result);
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


    // Package-private for testing
    String replacePreIncrementDecrement(String token) {
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

    // Package-private for testing
    String replacePostIncrementDecrement(String token) {
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


    // Package-private for testing
    String calculateCleanExpression(String expression) {
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

        String b = String.join(" ", tokens).replaceAll("\\s+", " ").trim();
        String[] tokens2 = b.isEmpty() ? new String[0] : b.split(" ");
        for (int i = 0; i < tokens2.length; i++) {
            if ((Arrays.asList("+", "-").contains(tokens2[i]))) {
                tokens2[i + 1] = String.valueOf(calculate(Double.parseDouble(tokens2[i - 1]), Double.parseDouble(tokens2[i + 1]), tokens2[i]));
                tokens2[i] = "";
                tokens2[i - 1] = "";
                i++;
            }
        }
        return String.join(" ", tokens2).replaceAll("\\s+", " ").trim();
    }

    private Double calculate(double v, double v1, String operator) {
        switch (operator) {
            case "*":
                return v * v1;
            case "/":
                return divide(v, v1);
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

    // Package-private for testing
    String openBrackets(String expression) {
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

        return String.join(" ", tokens).replaceAll("\\s+", " ").trim();
    }

    private static String[] getElements(String[] tokens, int i, int openIndex) {
        String joined = String.join(" ", Arrays.copyOfRange(tokens, openIndex + 1, i))
                              .replaceAll("\\s+", " ")
                              .trim();
        return joined.isEmpty() ? new String[0] : joined.split(" ");
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        // Example from specification
        System.out.println("Input: Following is a series of valid inputs for the program:");
        String[] expressions = {
            "i = 0",
            "j = ++i",
            "x = i++ + 5",
            "y = (5 + 3) * 10",
            "i += y"
        };

        for (String expr : expressions) {
            System.out.println(expr);
        }

        HashMap<String, Var> results = calculator.processExpressions(expressions);
        System.out.println("Output:");
        System.out.println(calculator.formatOutput(results));
        System.out.println();

        // Additional examples
        System.out.println("=== Example 2 ===");
        calculator = new Calculator();
        String[] expressions2 = {
            "a = 10",
            "b = 20",
            "c = ( a + b ) * 2"
        };
        System.out.println("Input:");
        for (String expr : expressions2) {
            System.out.println(expr);
        }
        HashMap<String, Var> results2 = calculator.processExpressions(expressions2);
        System.out.println("Output:");
        System.out.println(calculator.formatOutput(results2));
        System.out.println();

        System.out.println("=== Example 3 ===");
        calculator = new Calculator();
        String[] expressions3 = {
            "num = 100",
            "num += 50",
            "num *= 2",
            "num /= 4"
        };
        System.out.println("Input:");
        for (String expr : expressions3) {
            System.out.println(expr);
        }
        HashMap<String, Var> results3 = calculator.processExpressions(expressions3);
        System.out.println("Output:");
        System.out.println(calculator.formatOutput(results3));
    }
}