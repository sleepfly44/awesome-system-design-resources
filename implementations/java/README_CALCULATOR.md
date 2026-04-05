# Java Expression Calculator

A calculator that processes a series of assignment expressions supporting a subset of Java numeric expressions and operators.

## Features

### Supported Operators
- **Arithmetic**: `+`, `-`, `*`, `/`, `%`
- **Assignment**: `=`
- **Compound Assignment**: `+=`, `-=`, `*=`, `/=`, `%=`
- **Increment/Decrement**: `++`, `--` (both pre and post)
- **Parentheses**: `()` for grouping and precedence override

### Operator Precedence
Follows standard Java precedence (PEMDAS):
1. Parentheses `()`
2. Multiplication, Division, Modulo: `*`, `/`, `%`
3. Addition, Subtraction: `+`, `-`

### Features
- Variable storage and retrieval
- Pre-increment (`++i`) and post-increment (`i++`)
- Pre-decrement (`--i`) and post-decrement (`i--`)
- Nested parentheses
- Compound assignments

## Usage

### Input Format
A series of assignment expressions (one per line or in an array):

```java
i = 0
j = ++i
x = i++ + 5
y = (5 + 3) * 10
i += y
```

### Output Format
All variables with their final values in alphabetical order:
```
(i=82,j=1,x=6,y=80)
```

### Example Code

```java
Calculator calculator = new Calculator();

String[] expressions = {
    "i = 0",
    "j = ++i",
    "x = i++ + 5",
    "y = (5 + 3) * 10",
    "i += y"
};

HashMap<String, Double> results = calculator.processExpressions(expressions);
System.out.println(calculator.formatOutput(results));
// Output: (i=82,j=1,x=6,y=80)
```

## How It Works

### Expression Evaluation Steps
1. **Normalize**: Add spaces around parentheses
2. **Tokenize**: Split expression into tokens
3. **Process Tokens**: Handle increment/decrement and substitute variables
4. **Resolve Parentheses**: Evaluate innermost expressions first
5. **Apply Precedence**: Evaluate `*`, `/`, `%` before `+`, `-`
6. **Return Result**: Store in variable if assignment

### Walkthrough Example

Input:
```
i = 0      // i = 0
j = ++i    // i becomes 1, j = 1
x = i++ + 5  // x = 1 + 5 = 6, then i becomes 2
y = (5 + 3) * 10  // y = 8 * 10 = 80
i += y     // i = 2 + 80 = 82
```

Output: `(i=82,j=1,x=6,y=80)`

## Running the Code

### Compile
```bash
javac taboola/Calculator.java taboola/Var.java
```

### Run
```bash
java taboola.Calculator
```

### Run Tests
```bash
javac taboola/CalculatorTest.java
java taboola.CalculatorTest
```

## Class Structure

### `Calculator.java`
Main calculator class with expression evaluation logic.

**Key Methods:**
- `processExpressions(String[] expressions)` - Process array of expressions
- `evaluate(String expression)` - Evaluate single expression
- `formatOutput(HashMap<String, Double> results)` - Format output

### `Var.java`
Simple variable wrapper class storing name and value.

### `CalculatorTest.java`
Comprehensive unit tests with 74 test cases covering all features and edge cases.

## Test Coverage
- ✅ 74/74 tests passing (100%)
- Basic arithmetic operations
- Variable operations
- Increment/decrement (pre and post)
- Expression evaluation with precedence
- Parentheses handling (including nested)
- Simple and compound assignments
- Edge cases (negatives, decimals, large numbers)
- Error handling (division by zero, undefined variables, etc.)

## Examples

### Example 1: Basic Assignments
```java
i = 0
j = ++i
x = i++ + 5
y = 5 + 3 * 10
i += y
```
Output: `(i=37,j=1,x=6,y=35)`

### Example 2: Parentheses
```java
a = 10
b = 20
c = ( a + b ) * 2
```
Output: `(a=10,b=20,c=60)`

### Example 3: Compound Assignments
```java
num = 100
num += 50
num *= 2
num /= 4
```
Output: `(num=75)`

## Implementation Details

- **Language**: Java
- **Dependencies**: None (uses only standard Java libraries)
- **Package**: `taboola`
- **Precision**: Double-precision floating-point
- **Output**: Integers displayed without decimal points

## Error Handling

The calculator throws `IllegalArgumentException` for:
- Division by zero
- Undefined variables
- Invalid operators
- Mismatched parentheses
- Malformed expressions

## License
Part of the awesome-system-design-resources repository.
