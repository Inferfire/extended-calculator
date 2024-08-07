# Extended Calculator
#### _A modified recreation of the extended iOS Calculator created by Apple Inc._

### Purpose

- To create a functional calculator that can handle typical arithmetic operations such as addition, subtraction, multiplication, and division.
- The goal is to also be able perform a variety of advanced operations like exponentiation, logarithms, trigonometric functions, factorials, and some constants like π and e.
- Functionality to copy the current value on the display to the clipboard.
- Handling of repeated evaluations. E.g., entering "3 + 2 =" would produce an output of 5, however by pressing "=" again, an additional 2 should be added to the output, resulting in an output of 7.

### General Specifications

- The calculator displays on a frame labeled "Calculator".
- The main display of the calculator is at the top, where results and inputs are shown.
- Below the main display are various buttons. Each button represents a number, an operation, or another function like clearing the input.
- The buttons have different colors, indicating their functionality groups. For instance, number buttons have a different shade compared to operation buttons.
- Some buttons have rounded shapes, especially the four primary arithmetic operations and the equals sign.
- Buttons change their appearance when hovered over to provide a more interactive user experience.

### Version of Java and Testing Framework

- JDK 16, Maven build tool, used JUnit
