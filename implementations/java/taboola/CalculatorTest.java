package taboola;

/**
 * Comprehensive unit tests for Calculator class.
 * Tests all methods including edge cases.
 */
public class CalculatorTest {
    private Calculator calculator;
    private int passedTests = 0;
    private int failedTests = 0;

    public void setUp() {
        calculator = new Calculator();
        calculator.variables.clear();
    }

    // Helper methods for assertions
    private void assertEquals(double expected, double actual, String testName) {
        if (Math.abs(expected - actual) < 0.0001) {
            passedTests++;
            System.out.println("✓ PASS: " + testName);
        } else {
            failedTests++;
            System.out.println("✗ FAIL: " + testName + " - Expected: " + expected + ", Got: " + actual);
        }
    }

    private void assertEquals(String expected, String actual, String testName) {
        if (expected.equals(actual)) {
            passedTests++;
            System.out.println("✓ PASS: " + testName);
        } else {
            failedTests++;
            System.out.println("✗ FAIL: " + testName + " - Expected: '" + expected + "', Got: '" + actual + "'");
        }
    }

    private void assertTrue(boolean condition, String testName) {
        if (condition) {
            passedTests++;
            System.out.println("✓ PASS: " + testName);
        } else {
            failedTests++;
            System.out.println("✗ FAIL: " + testName);
        }
    }

    private void assertThrows(Runnable code, String testName) {
        try {
            code.run();
            failedTests++;
            System.out.println("✗ FAIL: " + testName + " - Expected exception but none was thrown");
        } catch (Exception e) {
            passedTests++;
            System.out.println("✓ PASS: " + testName + " - Exception thrown as expected: " + e.getClass().getSimpleName());
        }
    }

    // ==================== Tests for Basic Arithmetic Operations ====================

    public void testDivide_Normal() {
        setUp();
        assertEquals(5.0, calculator.divide(10, 2), "divide: 10/2");
    }

    public void testDivide_Decimal() {
        setUp();
        assertEquals(2.5, calculator.divide(5, 2), "divide: 5/2 with decimal result");
    }

    public void testDivide_Negative() {
        setUp();
        assertEquals(-5.0, calculator.divide(10, -2), "divide: 10/-2");
        assertEquals(-5.0, calculator.divide(-10, 2), "divide: -10/2");
        assertEquals(5.0, calculator.divide(-10, -2), "divide: -10/-2");
    }

    public void testDivide_ByZero() {
        setUp();
        assertThrows(() -> calculator.divide(10, 0), "divide: Division by zero throws exception");
    }

    public void testIncrement() {
        setUp();
        assertEquals(6.0, calculator.increment(5), "increment: 5");
        assertEquals(0.0, calculator.increment(-1), "increment: -1");
        assertEquals(-9.0, calculator.increment(-10), "increment: -10");
    }

    public void testDecrement() {
        setUp();
        assertEquals(4.0, calculator.decrement(5), "decrement: 5");
        assertEquals(-1.0, calculator.decrement(0), "decrement: 0");
        assertEquals(-11.0, calculator.decrement(-10), "decrement: -10");
    }

    // ==================== Tests for Variable Operations ====================

    public void testSetVariable_Normal() {
        setUp();
        calculator.setVariable("x", 10.5);
        assertTrue(calculator.variables.containsKey("x"), "setVariable: Variable 'x' exists");
        assertEquals(10.5, calculator.variables.get("x").getValue(), "setVariable: Variable 'x' has correct value");
    }

    public void testSetVariable_Override() {
        setUp();
        calculator.setVariable("x", 10);
        calculator.setVariable("x", 20);
        assertEquals(20.0, calculator.variables.get("x").getValue(), "setVariable: Variable override");
    }

    public void testSetVariable_MultipleVariables() {
        setUp();
        calculator.setVariable("a", 1);
        calculator.setVariable("b", 2);
        calculator.setVariable("c", 3);
        assertEquals(3, calculator.variables.size(), "setVariable: Multiple variables stored");
    }

    // ==================== Tests for Pre-Increment/Decrement ====================

    public void testReplacePreIncrement_Normal() {
        setUp();
        calculator.setVariable("x", 5);
        String result = calculator.replacePreIncrementDecrement("++x");
        assertEquals("6.0", result, "replacePreIncrementDecrement: ++x returns new value");
        assertEquals(6.0, calculator.variables.get("x").getValue(), "replacePreIncrementDecrement: ++x updates variable");
    }

    public void testReplacePreDecrement_Normal() {
        setUp();
        calculator.setVariable("x", 5);
        String result = calculator.replacePreIncrementDecrement("--x");
        assertEquals("4.0", result, "replacePreIncrementDecrement: --x returns new value");
        assertEquals(4.0, calculator.variables.get("x").getValue(), "replacePreIncrementDecrement: --x updates variable");
    }

    public void testReplacePreIncrement_UndefinedVariable() {
        setUp();
        assertThrows(() -> calculator.replacePreIncrementDecrement("++y"),
                     "replacePreIncrementDecrement: Undefined variable throws exception");
    }

    public void testReplacePreIncrement_InvalidOperator() {
        setUp();
        calculator.setVariable("x", 5);
        assertThrows(() -> calculator.replacePreIncrementDecrement("**x"),
                     "replacePreIncrementDecrement: Invalid operator throws exception");
    }

    // ==================== Tests for Post-Increment/Decrement ====================

    public void testReplacePostIncrement_Normal() {
        setUp();
        calculator.setVariable("x", 5);
        String result = calculator.replacePostIncrementDecrement("x++");
        assertEquals("5.0", result, "replacePostIncrementDecrement: x++ returns old value");
        assertEquals(6.0, calculator.variables.get("x").getValue(), "replacePostIncrementDecrement: x++ updates variable");
    }

    public void testReplacePostDecrement_Normal() {
        setUp();
        calculator.setVariable("x", 5);
        String result = calculator.replacePostIncrementDecrement("x--");
        assertEquals("5.0", result, "replacePostIncrementDecrement: x-- returns old value");
        assertEquals(4.0, calculator.variables.get("x").getValue(), "replacePostIncrementDecrement: x-- updates variable");
    }

    public void testReplacePostIncrement_UndefinedVariable() {
        setUp();
        assertThrows(() -> calculator.replacePostIncrementDecrement("y++"),
                     "replacePostIncrementDecrement: Undefined variable throws exception");
    }

    public void testReplacePostIncrement_InvalidOperator() {
        setUp();
        calculator.setVariable("x", 5);
        assertThrows(() -> calculator.replacePostIncrementDecrement("x**"),
                     "replacePostIncrementDecrement: Invalid operator throws exception");
    }

    // ==================== Tests for Expression Evaluation ====================

    public void testCalculateCleanExpression_SingleNumber() {
        setUp();
        String result = calculator.calculateCleanExpression("5");
        assertEquals("5", result.trim(), "calculateCleanExpression: Single number");
    }

    public void testCalculateCleanExpression_MultiplicationFirst() {
        setUp();
        String result = calculator.calculateCleanExpression("2 + 3 * 4");
        assertEquals("14.0", result, "calculateCleanExpression: Multiplication before addition");
    }

    public void testCalculateCleanExpression_DivisionFirst() {
        setUp();
        String result = calculator.calculateCleanExpression("10 + 20 / 4");
        assertEquals("15.0", result, "calculateCleanExpression: Division before addition");
    }

    public void testCalculateCleanExpression_ModuloFirst() {
        setUp();
        String result = calculator.calculateCleanExpression("10 + 7 % 3");
        assertEquals("11.0", result, "calculateCleanExpression: Modulo before addition");
    }

    public void testCalculateCleanExpression_LeftToRight() {
        setUp();
        String result = calculator.calculateCleanExpression("10 - 3 - 2");
        assertEquals("5.0", result, "calculateCleanExpression: Left to right for same precedence");
    }

    public void testCalculateCleanExpression_AllOperators() {
        setUp();
        String result = calculator.calculateCleanExpression("1 * 8 - 7 + 6 / 2 + 3 % 2");
        assertEquals("5.0", result, "calculateCleanExpression: All operators");
    }

    // ==================== Tests for Parentheses ====================

    public void testOpenBrackets_Simple() {
        setUp();
        String result = calculator.openBrackets("( 2 + 3 )");
        assertEquals("5.0", result, "openBrackets: Simple parentheses");
    }

    public void testOpenBrackets_Nested() {
        setUp();
        String result = calculator.openBrackets("( 1 + ( 2 + 3 ) )");
        assertEquals("6.0", result, "openBrackets: Nested parentheses");
    }

    public void testOpenBrackets_MultipleNested() {
        setUp();
        String result = calculator.openBrackets("( 1 * ( ( 8 - 7 ) ) + 6 / ( 2 + 3 ) % 2 )");
        assertTrue(result.contains("2.2") || result.contains("2.19"), "openBrackets: Multiple nested parentheses");
    }

    public void testOpenBrackets_WithOperatorPrecedence() {
        setUp();
        String result = calculator.openBrackets("( 2 + 3 ) * 4");
        // After opening brackets, we should have "5.0 * 4" or similar
        String finalResult = calculator.calculateCleanExpression(result);
        assertEquals("20.0", finalResult, "openBrackets: Parentheses override precedence");
    }

    public void testOpenBrackets_MismatchedParentheses() {
        setUp();
        assertThrows(() -> calculator.openBrackets("( 2 + 3"),
                     "openBrackets: Mismatched parentheses throws exception");
    }

    // ==================== Tests for Simple Assignment ====================

    public void testEvaluate_SimpleAssignment() {
        setUp();
        calculator.setVariable("x", 5);
        double result = calculator.evaluate("a = x + 10");
        assertEquals(15.0, result, "evaluate: Simple assignment");
        assertEquals(15.0, calculator.variables.get("a").getValue(), "evaluate: Variable stored correctly");
    }

    public void testEvaluate_AssignmentWithOperatorPrecedence() {
        setUp();
        calculator.setVariable("x", 5);
        calculator.setVariable("c", 2);
        double result = calculator.evaluate("a = x + 8 + c * 3");
        assertEquals(19.0, result, "evaluate: Assignment with operator precedence");
    }

    public void testEvaluate_AssignmentWithParentheses() {
        setUp();
        calculator.setVariable("p", 4);
        calculator.setVariable("q", 6);
        double result = calculator.evaluate("answer = ( p + q ) * 2");
        assertEquals(20.0, result, "evaluate: Assignment with parentheses");
    }

    public void testEvaluate_ChainedAssignment() {
        setUp();
        calculator.setVariable("x", 10);
        calculator.evaluate("a = x + 5");
        calculator.evaluate("b = a * 2");
        assertEquals(30.0, calculator.variables.get("b").getValue(), "evaluate: Chained assignment");
    }

    // ==================== Tests for Compound Assignment ====================

    public void testEvaluate_CompoundAddition() {
        setUp();
        calculator.setVariable("num", 10);
        double result = calculator.evaluate("num += 5");
        assertEquals(15.0, result, "evaluate: Compound addition (+=)");
        assertEquals(15.0, calculator.variables.get("num").getValue(), "evaluate: Variable updated correctly");
    }

    public void testEvaluate_CompoundSubtraction() {
        setUp();
        calculator.setVariable("num", 10);
        double result = calculator.evaluate("num -= 3");
        assertEquals(7.0, result, "evaluate: Compound subtraction (-=)");
    }

    public void testEvaluate_CompoundMultiplication() {
        setUp();
        calculator.setVariable("num", 10);
        double result = calculator.evaluate("num *= 2");
        assertEquals(20.0, result, "evaluate: Compound multiplication (*=)");
    }

    public void testEvaluate_CompoundDivision() {
        setUp();
        calculator.setVariable("num", 10);
        double result = calculator.evaluate("num /= 2");
        assertEquals(5.0, result, "evaluate: Compound division (/=)");
    }

    public void testEvaluate_CompoundModulo() {
        setUp();
        calculator.setVariable("num", 10);
        double result = calculator.evaluate("num %= 3");
        assertEquals(1.0, result, "evaluate: Compound modulo (%=)");
    }

    public void testEvaluate_CompoundWithExpression() {
        setUp();
        calculator.setVariable("num", 10);
        calculator.setVariable("x", 5);
        double result = calculator.evaluate("num += x * 2");
        assertEquals(20.0, result, "evaluate: Compound with expression");
    }

    public void testEvaluate_CompoundUndefinedVariable() {
        setUp();
        assertThrows(() -> calculator.evaluate("num += 5"),
                     "evaluate: Compound assignment on undefined variable throws exception");
    }

    // ==================== Tests for Full Expression Evaluation ====================

    public void testEvaluate_NumbersOnly() {
        setUp();
        double result = calculator.evaluate("10 + 5 * 2");
        assertEquals(20.0, result, "evaluate: Numbers only with precedence");
    }

    public void testEvaluate_WithVariables() {
        setUp();
        calculator.setVariable("x", 5);
        calculator.setVariable("y", 3);
        double result = calculator.evaluate("x + y * 2");
        assertEquals(11.0, result, "evaluate: Expression with variables");
    }

    public void testEvaluate_WithPreIncrement() {
        setUp();
        calculator.setVariable("x", 5);
        double result = calculator.evaluate("++x + 10");
        assertEquals(16.0, result, "evaluate: Pre-increment in expression");
        assertEquals(6.0, calculator.variables.get("x").getValue(), "evaluate: Variable incremented");
    }

    public void testEvaluate_WithPostIncrement() {
        setUp();
        calculator.setVariable("x", 5);
        double result = calculator.evaluate("x++ + 10");
        assertEquals(15.0, result, "evaluate: Post-increment in expression (uses old value)");
        assertEquals(6.0, calculator.variables.get("x").getValue(), "evaluate: Variable incremented after use");
    }

    public void testEvaluate_ComplexWithAllFeatures() {
        setUp();
        calculator.setVariable("x", 5);
        calculator.setVariable("b", 2);
        calculator.setVariable("d", 3);
        double result = calculator.evaluate("x++ + ++b * ( d - 8 ) + x");
        assertEquals(-4.0, result, "evaluate: Complex expression with all features");
    }

    public void testEvaluate_WithParentheses() {
        setUp();
        double result = calculator.evaluate("( 2 + 3 ) * ( 4 + 1 )");
        assertEquals(25.0, result, "evaluate: Multiple parentheses groups");
    }

    public void testEvaluate_NestedParentheses() {
        setUp();
        double result = calculator.evaluate("( ( 2 + 3 ) * 2 )");
        assertEquals(10.0, result, "evaluate: Nested parentheses");
    }

    // ==================== Edge Cases ====================

    public void testEvaluate_NegativeNumbers() {
        setUp();
        calculator.setVariable("x", -5);
        calculator.setVariable("y", -3);
        double result = calculator.evaluate("x + y");
        assertEquals(-8.0, result, "evaluate: Negative numbers");
    }

    public void testEvaluate_LargeNumbers() {
        setUp();
        calculator.setVariable("x", 1000000);
        calculator.setVariable("y", 2000000);
        double result = calculator.evaluate("x + y");
        assertEquals(3000000.0, result, "evaluate: Large numbers");
    }

    public void testEvaluate_DecimalNumbers() {
        setUp();
        calculator.setVariable("x", 3.14);
        calculator.setVariable("y", 2.86);
        double result = calculator.evaluate("x + y");
        assertEquals(6.0, result, "evaluate: Decimal numbers");
    }

    public void testEvaluate_DivisionByZeroInExpression() {
        setUp();
        assertThrows(() -> calculator.evaluate("10 / 0"),
                     "evaluate: Division by zero in expression throws exception");
    }

    public void testEvaluate_ZeroValues() {
        setUp();
        calculator.setVariable("x", 0);
        double result = calculator.evaluate("x + 5");
        assertEquals(5.0, result, "evaluate: Zero value variable");
    }

    public void testEvaluate_SingleValue() {
        setUp();
        calculator.setVariable("x", 42);
        double result = calculator.evaluate("x");
        assertEquals(42.0, result, "evaluate: Single variable");
    }

    public void testEvaluate_SingleNumber() {
        setUp();
        double result = calculator.evaluate("42");
        assertEquals(42.0, result, "evaluate: Single number");
    }

    public void testEvaluate_ComplexNestedExpression() {
        setUp();
        calculator.setVariable("a", 2);
        calculator.setVariable("b", 3);
        calculator.setVariable("c", 4);
        double result = calculator.evaluate("( a + b ) * ( c - 1 )");
        assertEquals(15.0, result, "evaluate: Complex nested expression with variables");
    }

    public void testEvaluate_MultipleIncrements() {
        setUp();
        calculator.setVariable("x", 1);
        calculator.setVariable("y", 1);
        double result = calculator.evaluate("++x + ++y + x + y");
        assertEquals(8.0, result, "evaluate: Multiple increments - (2 + 2 + 2 + 2)");
    }

    public void testEvaluate_MixedIncrements() {
        setUp();
        calculator.setVariable("x", 5);
        double result = calculator.evaluate("x++ + ++x");
        // x++ returns 5, x becomes 6, ++x makes x=7 and returns 7
        assertEquals(12.0, result, "evaluate: Mixed post and pre increment");
        assertEquals(7.0, calculator.variables.get("x").getValue(), "evaluate: Final value after mixed increments");
    }

    // ==================== Tests for Invalid Input ====================

    public void testEvaluate_InvalidAssignment() {
        setUp();
        assertThrows(() -> calculator.evaluate("a = "),
                     "evaluate: Invalid assignment (missing right side)");
    }

    public void testEvaluate_MultipleEquals() {
        setUp();
        assertThrows(() -> calculator.evaluate("a = b = 5"),
                     "evaluate: Multiple equals signs");
    }

    // ==================== Run All Tests ====================

    public void runAllTests() {
        System.out.println("\n========================================");
        System.out.println("  CALCULATOR UNIT TESTS");
        System.out.println("========================================\n");

        // Basic arithmetic
        System.out.println("--- Basic Arithmetic Operations ---");
        testDivide_Normal();
        testDivide_Decimal();
        testDivide_Negative();
        testDivide_ByZero();
        testIncrement();
        testDecrement();

        // Variable operations
        System.out.println("\n--- Variable Operations ---");
        testSetVariable_Normal();
        testSetVariable_Override();
        testSetVariable_MultipleVariables();

        // Pre-increment/decrement
        System.out.println("\n--- Pre-Increment/Decrement ---");
        testReplacePreIncrement_Normal();
        testReplacePreDecrement_Normal();
        testReplacePreIncrement_UndefinedVariable();
        testReplacePreIncrement_InvalidOperator();

        // Post-increment/decrement
        System.out.println("\n--- Post-Increment/Decrement ---");
        testReplacePostIncrement_Normal();
        testReplacePostDecrement_Normal();
        testReplacePostIncrement_UndefinedVariable();
        testReplacePostIncrement_InvalidOperator();

        // Expression evaluation
        System.out.println("\n--- Expression Evaluation ---");
        testCalculateCleanExpression_SingleNumber();
        testCalculateCleanExpression_MultiplicationFirst();
        testCalculateCleanExpression_DivisionFirst();
        testCalculateCleanExpression_ModuloFirst();
        testCalculateCleanExpression_LeftToRight();
        testCalculateCleanExpression_AllOperators();

        // Parentheses
        System.out.println("\n--- Parentheses Handling ---");
        testOpenBrackets_Simple();
        testOpenBrackets_Nested();
        testOpenBrackets_MultipleNested();
        testOpenBrackets_WithOperatorPrecedence();
        testOpenBrackets_MismatchedParentheses();

        // Simple assignment
        System.out.println("\n--- Simple Assignment ---");
        testEvaluate_SimpleAssignment();
        testEvaluate_AssignmentWithOperatorPrecedence();
        testEvaluate_AssignmentWithParentheses();
        testEvaluate_ChainedAssignment();

        // Compound assignment
        System.out.println("\n--- Compound Assignment ---");
        testEvaluate_CompoundAddition();
        testEvaluate_CompoundSubtraction();
        testEvaluate_CompoundMultiplication();
        testEvaluate_CompoundDivision();
        testEvaluate_CompoundModulo();
        testEvaluate_CompoundWithExpression();
        testEvaluate_CompoundUndefinedVariable();

        // Full expression evaluation
        System.out.println("\n--- Full Expression Evaluation ---");
        testEvaluate_NumbersOnly();
        testEvaluate_WithVariables();
        testEvaluate_WithPreIncrement();
        testEvaluate_WithPostIncrement();
        testEvaluate_ComplexWithAllFeatures();
        testEvaluate_WithParentheses();
        testEvaluate_NestedParentheses();

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        testEvaluate_NegativeNumbers();
        testEvaluate_LargeNumbers();
        testEvaluate_DecimalNumbers();
        testEvaluate_DivisionByZeroInExpression();
        testEvaluate_ZeroValues();
        testEvaluate_SingleValue();
        testEvaluate_SingleNumber();
        testEvaluate_ComplexNestedExpression();
        testEvaluate_MultipleIncrements();
        testEvaluate_MixedIncrements();

        // Invalid input
        System.out.println("\n--- Invalid Input Handling ---");
        testEvaluate_InvalidAssignment();
        testEvaluate_MultipleEquals();

        // Summary
        System.out.println("\n========================================");
        System.out.println("  TEST SUMMARY");
        System.out.println("========================================");
        System.out.println("Total tests: " + (passedTests + failedTests));
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        if (failedTests == 0) {
            System.out.println("\n🎉 ALL TESTS PASSED! 🎉");
        } else {
            System.out.println("\n⚠️  SOME TESTS FAILED ⚠️");
        }
        System.out.println("========================================\n");
    }

    public static void main(String[] args) {
        CalculatorTest test = new CalculatorTest();
        test.runAllTests();
    }
}
