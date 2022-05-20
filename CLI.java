package processor;

import java.util.Scanner;
import java.util.function.Supplier;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix by a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Transpose matrix");
            System.out.println("5. Calculate a determinant");
            System.out.println("6. Inverse matrix");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            String input = scanner.nextLine().strip();
            switch (input) {
                case "1":
                    add();
                    break;
                case "2":
                    multiplyByScalar();
                    break;
                case "3":
                    multiply();
                    break;
                case "4":
                    transpose();
                    break;
                case "5":
                    determinant();
                    break;
                case "6":
                    inverse();
                    break;
                case "0":
                    return;
                default:
                    break;
            }
        }
    }

    private void add() {
        Matrix a = Matrix.parse("first");
        Matrix b = Matrix.parse("second");
        try {
            System.out.printf("The result is: %n%s%n", a.add(b));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void multiplyByScalar() {
        Matrix a = Matrix.parse();
        System.out.print("Enter constant: ");
        System.out.printf("The result is: %n%s%n", a.scale(scanner.nextDouble()));
        scanner.nextLine();
    }

    private void multiply() {
        Matrix a = Matrix.parse("first");
        Matrix b = Matrix.parse("second");
        try {
            System.out.printf("The result is: %n%s%n", a.multiply(b));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void transpose() {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");

        String input = scanner.nextLine().strip();
        if (!input.matches("[1234]")) {
            return;
        }
        Matrix matrix = Matrix.parse();
        Supplier<Matrix> transposeFunction;
        switch (input) {
            case "1":
                transposeFunction = matrix::transposeMain;
                break;
            case "2":
                transposeFunction = matrix::transposeSide;
                break;
            case "3":
                transposeFunction = matrix::transposeAlongVerticalLine;
                break;
            case "4":
                transposeFunction = matrix::transposeAlongHorizontalLine;
                break;
            default:
                return;
        }
        System.out.printf("The result is: %n%s%n", transposeFunction.get());
    }

    private void determinant() {
        Matrix a = Matrix.parse();
        try {
            System.out.printf("The result is: %n%s%n%n", formatNum(a.determinant()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void inverse() {
        Matrix a = Matrix.parse();
        try {
            System.out.printf("The result is: %n%s%n%n", a.inverse());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String formatNum(double num) {
        return Math.floor(num) == num ? String.format("%.0f", num) : String.valueOf(num);
    }
}
