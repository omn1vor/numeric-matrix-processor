package processor;

import java.util.Scanner;

public class Matrix {
    private final int n;
    private final int m;
    double[][] matrix;

    public Matrix(int n, int m, double[][] matrix) {
        this.n = n;
        this.m = m;
        this.matrix = matrix.clone();
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public double get(int i, int j) {
        return matrix[i][j];
    }

    public double[][] getMatrix() {
        return matrix.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            for (double num : row) {
                sb.append(String.format(num % 1.0 == 0 ? "% 5.0f" : "% 5.2f", num));
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static Matrix parse() {
        return parse("");
    }

    public static Matrix parse(String matrixName) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Enter size of %s matrix: ", matrixName);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.printf("Enter %s matrix: ", matrixName);
        double[][] data = new double[rows][cols];
        scanner.nextLine();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = scanner.nextDouble();
            }
        }
        return new Matrix(rows, cols, data);
    }

    public Matrix add(Matrix other) {
        if (!hasSameDimensions(other)) {
            throw new IllegalArgumentException("The operation cannot be performed.");
        }
        double[][] data = matrix.clone();
        double[][] otherData = other.getMatrix();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] += otherData[i][j];
            }
        }
        return new Matrix(n, m, data);
    }

    public Matrix scale(double scalar) {
        double[][] data = matrix.clone();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] *= scalar;
            }
        }
        return new Matrix(n, m, data);
    }

    public Matrix multiply(Matrix other) {
        if (m != other.getN()) {
            throw new IllegalArgumentException("The operation cannot be performed.");
        }
        int rows = n;
        int cols = other.getM();
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += matrix[i][k] * other.get(k, j);
                }
                data[i][j] = sum;
            }
        }
        return new Matrix(rows, cols, data);
    }

    public Matrix transposeMain() {
        double[][]data = new double[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[j][i] = matrix[i][j];
            }
        }
        return new Matrix(m, n, data);
    }

    public Matrix transposeSide() {
        double[][]data = new double[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[j][i] = matrix[n - i - 1][m - j - 1];
            }
        }
        return new Matrix(m, n, data);
    }

    public Matrix transposeAlongVerticalLine() {
        double[][]data = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = matrix[i][m - j - 1];
            }
        }
        return new Matrix(n, m, data);
    }

    public Matrix transposeAlongHorizontalLine() {
        double[][]data = new double[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[n - i - 1], 0, data[i], 0, m);
        }
        return new Matrix(n, m, data);
    }

    public double determinant() {
        if (n != m) {
            throw new IllegalArgumentException("The operation cannot be performed.");
        }
        if (n == 1) {
            return matrix[0][0];
        } else if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        double d = 0;
        for (int x = 0; x < n; x++) {
            d += matrix[0][x] * cofactor(0, x);
        }
        return d;
    }

    public Matrix inverse() {
        double det = determinant();
        if (det == 0) {
            throw new IllegalArgumentException("This matrix doesn't have an inverse.");
        }
        double[][] cofactors = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cofactors[i][j] = cofactor(i, j);
            }
        }
        Matrix cofactorsTransposed = new Matrix(n, m, cofactors).transposeMain();
        return cofactorsTransposed.scale(1D / det);

    }

    private double cofactor(int xRow, int xCol) {
        double[][] data = new double[n - 1][n - 1];
        int row = 0;
        for (int i = 0; i < n; i++) {
            if (i == xRow) {
                continue;
            }
            int col = 0;
            for (int j = 0; j < n; j++) {
                if (j == xCol) {
                    continue;
                }
                data[row][col] = matrix[i][j];
                col++;
            }
            row++;
        }
        return Math.pow(-1, xRow + xCol) * new Matrix(n - 1, n - 1, data).determinant();
    }

    private boolean hasSameDimensions(Matrix other) {
        return n == other.getN() && m == other.getM();
    }

}
