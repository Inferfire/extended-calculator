import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    // declare the calculator, but don't initialize here
    Calculator calculator;

    @BeforeEach
    public void setUp() {
        // initialize the calculator before each test
        calculator = Calculator.getInstance();
    }

    // utility method to simulate button presses
    private void pressButtons(String... buttons) {
        for (String button : buttons) {
            calculator.processButton(button);
        }
    }

    @Test
    public void testAddition() {
        pressButtons("1", "+", "1", "=");
        Assertions.assertEquals("2", calculator.display.getText());

        pressButtons("1", "0", "0", "+", "2", "0", "0", "=");
        Assertions.assertEquals("300", calculator.display.getText());
    }

    @Test
    public void testSubtraction() {
        pressButtons("5", "–", "3", "=");
        Assertions.assertEquals("2", calculator.display.getText());
    }

    @Test
    public void testMultiplication() {
        pressButtons("5", "×", "2", "=");
        Assertions.assertEquals("10", calculator.display.getText());
    }

    @Test
    public void testDivision() {
        pressButtons("9", "÷", "3", "=");
        Assertions.assertEquals("3", calculator.display.getText());
    }

    @Test
    public void testSquareRoot() {
        pressButtons("9", "√x");
        Assertions.assertEquals("3", calculator.display.getText());
    }

    @Test
    public void testDecimals() {
        pressButtons("0", ".", "5", "×", "2", "=");
        Assertions.assertEquals("1", calculator.display.getText());
    }

    @Test
    public void testSquare() {
        pressButtons("3", "x^2");
        Assertions.assertEquals("9", calculator.display.getText());
    }

    @Test
    public void testMultipleOperations() {
        pressButtons("3", "+", "5", "=", "×", "2", "=");
        Assertions.assertEquals("16", calculator.display.getText());
    }

    @Test
    public void testReset() {
        pressButtons("1", "+", "1", "AC");
        Assertions.assertEquals("0", calculator.display.getText());
    }

    @Test
    public void testReciprocal() {
        pressButtons("4", "1/x");
        Assertions.assertEquals("0.25", calculator.display.getText());
    }

    @Test
    public void testNegativeNumbers() {
        pressButtons("5", "–", "7", "=");
        Assertions.assertEquals("-2", calculator.display.getText());
    }

    @Test
    public void testFactorial() {
        pressButtons("5", "x!");
        Assertions.assertEquals("120", calculator.display.getText());
    }

    @Test
    public void testNaturalLog() {
        pressButtons("2", "ln");
        Assertions.assertEquals(String.valueOf(Math.log(2)), calculator.display.getText());
    }

    @Test
    public void testLogBase10() {
        pressButtons("1", "0", "0", "log10");
        Assertions.assertEquals("2", calculator.display.getText());
    }

    @Test
    public void testTrigonometricFunctions() {
        pressButtons("0", "sin");
        Assertions.assertEquals("0", calculator.display.getText());

        pressButtons("0", "cos");
        Assertions.assertEquals("1", calculator.display.getText());

        pressButtons("0", "tan");
        Assertions.assertEquals("0", calculator.display.getText());
    }

    @Test
    public void testInverseTrigonometricFunctions() {
        pressButtons("0", "sinh");
        Assertions.assertEquals("0", calculator.display.getText());

        pressButtons("0", "cosh");
        Assertions.assertEquals("1", calculator.display.getText());

        pressButtons("0", "tanh");
        Assertions.assertEquals("0", calculator.display.getText());
    }

    @Test
    public void testRandomFunction() {
        pressButtons("Rand");
        double value = Double.parseDouble(calculator.display.getText());
        Assertions.assertTrue(value >= 0 && value <= 1);
    }

    @Test
    public void testDivisionByZero() {
        pressButtons("5", "÷", "0", "=");
        Assertions.assertEquals("Error", calculator.display.getText());
    }

    @Test
    public void testChainedOperations() {
        pressButtons("2", "+", "3", "×", "4", "=");
        Assertions.assertEquals("20", calculator.display.getText());

        pressButtons("2", "+", "3", "=", "×", "4", "=");
        Assertions.assertEquals("20", calculator.display.getText());
    }

    @Test
    public void testRepeatedEquals() {
        pressButtons("2", "+", "3", "=");
        Assertions.assertEquals("5", calculator.display.getText());
        pressButtons("=");
        Assertions.assertEquals("8", calculator.display.getText());
        pressButtons("=");
        Assertions.assertEquals("11", calculator.display.getText());
    }
}