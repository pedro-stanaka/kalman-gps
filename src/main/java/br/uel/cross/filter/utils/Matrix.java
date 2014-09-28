package br.uel.cross.filter.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.ejml.simple.SimpleMatrix;

/**
 *
 */
public class Matrix {

    double data[][];
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double data[][]) {
        this.data = data;
        this.cols = data[0].length;
        this.rows = data.length;
    }

    public void setMatrix(double... doubles) {
        assert doubles.length == this.cols * this.rows;
        int count = 0;
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                this.data[i][j] = doubles[count++];
            }
        }
    }

    public void setIdentityMatrix() {
        assert isSquare();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (i == j) {
                    this.data[i][j] = 1.0;
                } else {
                    this.data[i][j] = 0.0;
                }
            }
        }
    }


    @Override
    public Matrix clone() throws CloneNotSupportedException {

        Matrix matrix = new Matrix(this.rows, this.cols);

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.data[i], 0, matrix.getData()[i], 0, this.cols);
        }

        return matrix;
    }


    public Matrix addMatrix(Matrix b) {
        return matrixOperation(b, Operator.ADDITION);
    }

    public Matrix subtractMatrix(Matrix b) {
        return matrixOperation(b, Operator.SUBTRACTION);
    }

    public Matrix multipliedBy(Matrix other) {
        assert this.cols == other.getRows();

        Matrix result = new Matrix(this.rows, other.getCols());

        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getCols(); j++) {
                result.getData()[i][j] = 0.0;

                // Calculate element c.data[i][j] via a dot product of one row of a
                // with one column of b
                for (int k = 0; k < this.cols; k++) {
                    result.getData()[i][j] += this.data[i][k] * other.getData()[k][j];
                }
            }
        }

        return result;
    }

    public void subtractFromIdentity() {
        assert isSquare();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (i == j) {
                    this.data[i][j] = this.data[i][j] - 1.0;
                }
            }
        }
    }


    /**
     * Multiply this instance with other matrix transposing it during multiplication.
     * @param otherTranspose The other matrix to be multiplied.
     * @return Matrix multiplied
     */
    public Matrix multiplyByTranspose(Matrix otherTranspose) {
        assert this.cols == otherTranspose.cols;
        assert this.cols == otherTranspose.getCols();


        Matrix result = new Matrix(this.rows, otherTranspose.rows);

        for (int i = 0; i < result.getRows(); ++i) {
            for (int j = 0; j < result.getCols(); ++j) {
                result.getData()[i][j] = 0.0;

                for (int k = 0; k < this.cols; k++) {
                    result.getData()[i][j] = this.data[i][k] * otherTranspose.getData()[j][k];
                }
            }
        }

        return result;

    }

    public Matrix transposeMatrix() {
        Matrix output = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                output.getData()[j][i] = this.data[i][j];
            }
        }

        return output;
    }


    public boolean equalMatrix(Matrix a, double tolerance) {
        assert(a.rows == this.rows);
        assert(a.cols == this.cols);
        for (int i = 0; i < a.rows; ++i) {
            for (int j = 0; j < a.cols; ++j) {
                if (Math.abs(a.getData()[i][j] - this.data[i][j]) > tolerance) {
                    return false;
                }
            }
        }
        return true;
    }

    public Matrix scaleMatrix(double scalar) {
        assert(scalar != 0.0);

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                result.getData()[i][j] *= scalar;
            }
        }
        return result;
    }


    /**
     * Using LU Decomposition to calculate the inverse.
     * May be a good idea to check if Jordan-Gauss used by the original author is better
     * @return
     */
    public Matrix inverse(){
        assert isSquare();
        Matrix inverse = new Matrix(this.rows, this.cols);
        SimpleMatrix matrix = new SimpleMatrix(this.data);
        matrix = matrix.invert();

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                inverse.getData()[i][j] = matrix.get(i,j);
            }
        }

        return inverse;
    }

    private void swapRows(int r1, int r2) {
        assert(r1 != r2);
        double tmp[] = this.data[r1];
        this.data[r1] = this.data[r2];
        this.data[r2] = tmp;
    }

    private void scaleRow(int r, double scalar) {
        assert(scalar != 0.0);
        for (int i = 0; i < this.cols; ++i) {
            this.data[r][i] *= scalar;
        }
    }

    /**
     *  Add scalar * row r2 to row r1.
     */
    private void shearRow(int r1, int r2, double scalar) {
        assert(r1 != r2);
        for (int i = 0; i < this.cols; ++i) {
            this.data[r1][i] += scalar * this.data[r2][i];
        }
    }











    private Matrix matrixOperation(Matrix other, Operator operator) {
        assert (this.cols == other.cols);
        assert (this.rows == other.rows);

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < other.rows; ++i) {
            for (int j = 0; j < other.cols; ++j) {
                result.getData()[i][j] = operator.apply(this.data[i][j], other.data[i][j]);
            }
        }

        return result;
    }

    private boolean isSquare() {
        return this.cols == this.rows;
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public void print() {
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                if (j > 0) {
                    System.out.print(" ");
                }
                System.out.print(this.data[i][j]);
            }
            System.out.println("");
        }
    }

    public double getData(int row, int col) {
        return this.data[row][col];
    }

    protected enum Operator {
        ADDITION("+") {
            @Override
            public double apply(double x1, double x2) {
                return x1 + x2;
            }
        },
        SUBTRACTION("-") {
            @Override
            public double apply(double x1, double x2) {
                return x1 - x2;
            }
        },
        MULTIPLICATION("*") {
            @Override
            public double apply(double x1, double x2) {
                return x1 * x2;
            }
        };

        private final String text;

        private Operator(String text) {
            this.text = text;
        }

        public abstract double apply(double x1, double x2);



        @Override
        public String toString() {
            return text;
        }
    }
}
