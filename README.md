# Extended Calculator
#### _A modified recreation of the extended iOS Calculator created by Apple Inc._

### Purpose

- Create a calculator that minimizes code smells and adheres to SOLID / clean architecture principles.
- Handling of typical arithmetic operations such as addition, subtraction, multiplication, and division.
- Handling advanced operations like exponentiation, logarithms, trigonometric functions, factorials, and some constants like π and e.
- Handling of repeated evaluations. E.g., entering "3 + 2 =" would produce an output of 5, however by pressing "=" again, an additional 2 should be added to the output, resulting in an output of 7.
- Copying functionality for the current value on the display (to the clipboard).

### General Specifications

- The calculator displays on a frame labeled "Calculator".
- The main display of the calculator is at the top, where results and inputs are shown.
- Below the main display are various buttons. Each button represents a number, an operation, or another function like clearing the input.
- The buttons have different colors, indicating their functionality groups. For instance, number buttons have a different shade compared to operation buttons.
- Some buttons have rounded shapes, especially the four primary arithmetic operations and the equals sign.
- Buttons change their appearance when hovered over to provide a more interactive user experience.

### Version of Java and Testing Framework

- Java Version: JDK 16.0.2
- Build Tool: Maven
- Testing Framework: JUnit

### Code Smells and Violations of SOLID or CA principles

- **Single Responsibility Principle (SRP) Violation**: The Calculator class handles GUI creation, business logic, and event handling. These responsibilities should have been split among multiple classes.
- **Open/Closed Principle Violation**: For adding new features (like additional mathematical operations), slight modifications to existing code (e.g., the switch-case statements in doMathN4()) would be needed.
- **Liskov Substitution Principle (LSP)**: No clear violations, as the code doesn't really make use of inheritance where this could be a concern.
- **Interface Segregation Principle (ISP)**: No interfaces force any unnecessary implementations.
- **Tight Coupling**: The GUI and the calculator logic are tightly coupled. The calculator logic should be in its own class, and the GUI would simply call methods on that class.
