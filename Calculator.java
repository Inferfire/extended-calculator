import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

// constructor for the Calculator class; initializes the user interface
public class Calculator {
    // singleton pattern: Only one instance of Calculator class can be created
    private static final Calculator instance = new Calculator();
    private final JFrame frame; // main JFrame

    // calculator display
    final JTextField display = new JTextField("0");

    // unary calculation button grouping
    private final String n4Functions = "x\\^2|x\\^3|e\\^x|10\\^x|Copy|x!|ln|" +
            "log10|1/x|√x|∛x|e|sin|cos|tan|π|sinh|cosh|tanh|Rand";

    // unary calculation button grouping
    private final String fourFunctions = "[×÷+–]";

    private Double prevNumber = null;
    private Operator prevOperator = null;
    private boolean operatorPressed = false;
    private boolean resultDisplayed = false;
    private Double lastBinaryNumber = null;

    // operator enum for easy management of operator precedence and evaluation
    enum Operator {
        ADD("+"),
        SUBTRACT("–"),
        MULTIPLY("×"),
        DIVIDE("÷"),
        POWER("^"); // added for power operation

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public static Operator fromString(String symbol) {
            for (Operator op : Operator.values()) {
                if (op.getSymbol().equals(symbol)) {
                    return op;
                }
            }
            return null;
        }
    }

    private Calculator() {
        // initializes calculator UI components
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(522, 400);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#000000"));
        frame.setLocationRelativeTo(null);

        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBounds(2, 2, 517, 70);
        display.setEditable(false);
        display.setFocusable(false);
        display.setForeground(Color.WHITE);
        display.setFont(new Font("Monsterrat", Font.PLAIN, 28));
        Color backgroundColor = new Color(0, 0, 0, 0); // transparent
        display.setBackground(backgroundColor);
        frame.add(display);

        // button name representation
        String[] buttons = {
                "x^2", "x^3", "e^x", "10^x", "AC", "±", "%", "÷",
                "Copy", "x!", "ln", "log10", "7", "8", "9", "×",
                "1/x", "√x", "∛x", "e", "4", "5", "6", "–",
                "sin", "cos", "tan", "π", "1", "2", "3", "+",
                "sinh", "cosh", "tanh", "Rand", "0", ".", "="
        };

        int horizontalSpacing = 5; // between buttons
        int verticalSpacing = 9;   // between buttons
        int buttonHeight = 52;
        // int rows = 6;
        int cols = 8;

        // initializing buttons and their properties
        for (int i = 0; i < buttons.length; i++) {
            String colour = "#fe9f06";
            String fontColour = "#FFFFFF";
            int buttonWidth = 60;

            if (buttons[i].matches(n4Functions)) {
                colour = "#212121";
            } else if (buttons[i].equals("AC") || buttons[i].equals("±") ||
                    buttons[i].equals("%")) {
                colour = "#a5a5a5";
                fontColour = "#121212";
            } else if (buttons[i].matches("[0-9.]")) {
                colour = "#333333";
            }

            // special handling for the "0" button to make it span two columns
            if (buttons[i].equals("0")) {
                buttonWidth = 2 * 60 + horizontalSpacing; // "0" button -> wider
            }

            JButton btn;

            if (buttons[i].matches(fourFunctions) || buttons[i].equals("=")) {
                btn = genRoundBtn(buttons[i], colour, true);
            } else {
                btn = genRoundBtn(buttons[i], colour, false);
            }

            // calculate x and y positions
            int x = 4 + (i % cols) * (60 + horizontalSpacing); // default x pos

            // adjust position for "." and "=" due to the wider "0" button
            if (buttons[i].equals(".") || buttons[i].equals("=")) {
                x += 60 + horizontalSpacing; // adjust for the combined "0" btn
            } else if (buttons[i].equals("0")) {
                // adjust x for "0" button to span two columns
                x = 4 + 4 * (60 + horizontalSpacing);
            }

            int y = 71 + (i / cols) * (buttonHeight + verticalSpacing);
            btn.setForeground(Color.decode(fontColour));
            btn.setFont(new Font("Helvetica", Font.BOLD, 14));
            btn.setBounds(x, y, buttonWidth, buttonHeight);
            frame.add(btn);

            // Button listener
            btn.addActionListener(e -> processButton(btn.getText()));
        }

        frame.setVisible(true);
    }

    // handles input logic based on the button pressed in a calculator
    void processButton(String buttonText) {

        // check if the display is showing an error message
        if (display.getText().equals("Error")) {
            if (buttonText.matches("[0-9.]")) {
                // replace "Error" with pressed number (or decimal)
                display.setText(buttonText.equals(".") ? "0." : buttonText);
                resultDisplayed = false;
                return;
            } else if (buttonText.equals("AC")) {
                clearInput(); // allow clearing the error with "AC"
            }
            return; // ignore any other operations while in error state
        }

        if (buttonText.matches("[0-9.]")) {
            if (resultDisplayed) {
                prevNumber = null;
                prevOperator = null;
                resultDisplayed = false;
                display.setText("0"); // resets display for new calculations
            }
            numberOrDecimalInput(buttonText);
        } else if (buttonText.equals("AC")) {
            prevOperator = null;
            clearInput();
        } else if (buttonText.matches(n4Functions) ||
                buttonText.matches("[%±]")) {
            doMathN4(buttonText);
        } else if (buttonText.matches(fourFunctions)) {
            if (resultDisplayed ||
                    (prevOperator == null && prevNumber == null)) {
                prevNumber = Double.parseDouble(display.getText());
                prevOperator = Operator.fromString(buttonText);
                operatorPressed = true;
                resultDisplayed = false;
            } else {
                doMath4F(buttonText);
            }
        } else if (buttonText.equals("=")) {
            evaluateEquals();
        }
    }

    // handles numerical or decimal input.
    private void numberOrDecimalInput(String buttonText) {
        String currentText = display.getText();

        if (operatorPressed || resultDisplayed) {
            currentText = "0";
            operatorPressed = false;
            resultDisplayed = false;
            lastBinaryNumber = null; // reset last binary number
        }

        if (currentText.equals("0")) {
            if (buttonText.equals(".")) {
                display.setText(currentText + buttonText);
            } else {
                display.setText(buttonText);
            }
        } else {
            if (buttonText.equals(".")) {
                if (!currentText.contains(".")) {
                    display.setText(currentText + buttonText);
                }
            } else {
                display.setText(currentText + buttonText);
            }
        }
    }

    // clears the input field of the calculator (and operations as needed)
    private void clearInput() {
        display.setText("0");
        prevNumber = null;
        prevOperator = null;
        lastBinaryNumber = null;
    }

    // does the instant (non-four function calculators) calculations
    private void doMathN4(String operation) {
        String currentText = display.getText();
        double value;
        try {
            value = Double.parseDouble(currentText);
        } catch (NumberFormatException e) {
            clearInput();
            return;
        }

        switch (operation) {
            case "x^2" -> value = Math.pow(value, 2);
            case "x^3" -> value = Math.pow(value, 3);
            case "e^x" -> value = Math.exp(value);
            case "10^x" -> value = Math.pow(10, value);
            case "Copy" -> {
                StringSelection stringSelection = new StringSelection(
                        display.getText());
                Clipboard clipboard =
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                JOptionPane.showMessageDialog(frame,
                        "Content copied to clipboard.");
            }
            case "1/x" -> {
                if (value != 0) {
                    value = 1 / value;
                } else {
                    display.setText("Error");
                    return;
                }
            }
            case "√x" -> {
                if (value >= 0) {
                    value = Math.sqrt(value);
                } else {
                    display.setText("Error");
                    return;
                }
            }
            case "∛x" -> value = Math.cbrt(value);
            case "x!" -> {
                if (value < 0 || value != (int) value) {
                    display.setText("Error");
                    return;
                }
                value = factorial(value);
            }
            case "ln" -> {
                if (value > 0) {
                    value = Math.log(value);
                } else {
                    display.setText("Error");
                    return;
                }
            }
            case "log10" -> {
                if (value > 0) {
                    value = Math.log10(value);
                } else {
                    display.setText("Error");
                    return;
                }
            }
            case "e" -> value = Math.E;
            case "sin" -> value = Math.sin(value);
            case "cos" -> value = Math.cos(value);
            case "tan" -> value = Math.tan(value);
            case "π" -> value = Math.PI;
            case "sinh" -> value = Math.sinh(value);
            case "cosh" -> value = Math.cosh(value);
            case "tanh" -> value = Math.tanh(value);
            case "Rand" -> value = Math.random();
            case "±" -> value = value * -1;
            case "%" -> value = value / 100;
            default -> {
                display.setText("Not Implemented");
                return;
            }
        }

        display.setText(formatResult(value));
        resultDisplayed = true;
        prevNumber = value;  // updates prevNumber w/ result of advanced f(x)
    }

    // computes the factorial of a given number (int, non-negative)
    private double factorial(double n) {
        if (n == 0) {
            return 1;
        }
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // handles math operations for basic four functions (+, -, *, /).
    private void doMath4F(String operation) {
        double currentNumber;

        try {
            currentNumber = Double.parseDouble(display.getText());
        } catch (NumberFormatException e) {
            clearInput();
            return;
        }

        if (prevOperator != null && prevNumber != null && !operatorPressed &&
                !resultDisplayed) {
            switch (prevOperator) {
                case ADD -> currentNumber = prevNumber + currentNumber;
                case SUBTRACT -> currentNumber = prevNumber - currentNumber;
                case MULTIPLY -> currentNumber = prevNumber * currentNumber;
                case DIVIDE -> {
                    if (currentNumber == 0) {
                        display.setText("Error");
                        return;
                    }
                    currentNumber = prevNumber / currentNumber;
                }
            }
            display.setText(formatResult(currentNumber));
        }
        prevNumber = currentNumber;

        prevOperator = Operator.fromString(operation);
        operatorPressed = true;
        resultDisplayed = false;  // reset resultDisplayed flag
    }

    // handles logic when the equals button is pressed.
    private void evaluateEquals() {
        double currentNumber;
        double secondOperand;

        try {
            currentNumber = Double.parseDouble(display.getText());
        } catch (NumberFormatException e) {
            display.setText("Error");
            return;
        }

        if (prevOperator != null) {
            if (lastBinaryNumber != null) {
                secondOperand = lastBinaryNumber;
            } else {
                secondOperand = currentNumber;
            }

            switch (prevOperator) {
                case ADD -> currentNumber = prevNumber + secondOperand;
                case SUBTRACT -> currentNumber = prevNumber - secondOperand;
                case MULTIPLY -> currentNumber = prevNumber * secondOperand;
                case DIVIDE -> {
                    if (secondOperand == 0) {
                        display.setText("Error");
                        return;
                    }
                    currentNumber = prevNumber / secondOperand;
                }
                default -> {
                    return;
                }
            }

            display.setText(formatResult(currentNumber));
            resultDisplayed = true;
            prevNumber = currentNumber;  // stores result as new previous number
            lastBinaryNumber = secondOperand;  // stores 2nd operand for repeats
        } else {
            resultDisplayed = true;
        }
    }

    // formats the result to display on the calculator's screen.
    private String formatResult(double result) {
        // mainly to remove ".0" for whole numbers.
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else return String.valueOf(result); // for errors / infinity output
    }

    // singleton pattern: Returns the single instance of the Calculator class
    public static Calculator getInstance() {
        return instance;
    }

    // main method to run the calculator program.
    public static void main(String[] args) {
        Calculator calculator = Calculator.getInstance();
        System.out.println("Calculator instance created: " + calculator);
    }

    private static JButton genRoundBtn(String text, String colorHex,
                                       boolean toggle) {

        // generates a rounded (hover-able) button for the calculator's UI
        Color defaultColor = Color.decode(colorHex);
        Color hoverColor = toggle ? Color.decode("#FFFFFF") :
                defaultColor.brighter();

        JButton roundedButton = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isHovering = Boolean.TRUE.equals(
                        getClientProperty("isHovering"));
                Color bgColor = isHovering ? hoverColor : defaultColor;
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                        getHeight(), 50, 50));
                FontMetrics metrics = g2.getFontMetrics();
                int x = text.equals("0") ? 25 : (getWidth() -
                        metrics.stringWidth(getText())) / 2; // adjusts "0" pos
                int y = (getHeight() - metrics.getHeight()) / 2 +
                        metrics.getAscent();
                g2.setColor(isHovering && toggle ? Color.decode("#fe9f06") :
                        getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        roundedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                roundedButton.setCursor(
                        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                roundedButton.putClientProperty("isHovering", true);
                roundedButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                roundedButton.setCursor(Cursor.getDefaultCursor());
                roundedButton.putClientProperty("isHovering", false);
                roundedButton.repaint();
            }
        });

        return roundedButton;
    }
}