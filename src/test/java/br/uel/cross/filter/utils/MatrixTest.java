package br.uel.cross.filter.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MatrixTest {


    private Matrix foo;
    private Matrix m42;
    private Matrix m23;
    private Matrix m24;
    private Matrix m32;


    @Before
    public void setUp() throws Exception {
        foo = new Matrix(4, 4);
        foo.setMatrix(1.0, 2.0, 3.0, 4.0,
                4.0, 1.0, 7.0, 9.0,
                0.0, 0.0, -4.0, -4.0,
                2.3, 3.4, 3.1, 0.0);

        m42 = new Matrix(new double[][]{{1.0, 4.0}, {3.0, 7.0}, {1.0, 2.0}, {3.0, 4.0}});
        m32 = new Matrix(new double[][]{ {-3.0, 4.0}, {-9999.0, 166.0}, {16.0, 1000.0} });
        m23 = new Matrix(new double[][]{{1.0, 4.0, 3.0}, {3.0, 7.0, 1.0}});
        m24 = new Matrix(new double[][]{{1.0, 4.0, 3.0, 5.0}, {3.0, 7.0, 1.0, 0.0}});
    }


    @Test
    public void testClone() throws Exception {
        Matrix clone = foo.clone();
        assertThat(clone.getData()[1][1], is(equalTo(foo.getData()[1][1])));
        assertThat(clone.getData()[2][2], is(equalTo(foo.getData()[2][2])));
    }

    @Test
    public void testInverse() {
        Matrix foo = new Matrix(4, 4);
        foo.setMatrix(
                1.0, 2.0, 3.0, 4.0,
                4.0, 1.0, 7.0, 9.0,
                0.0, 0.0, -4.0, -4.0,
                2.3, 3.4, 3.1, 0.0);
        Matrix fooCopy = null;
        try {
            fooCopy = foo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Matrix bar;
        Matrix identity = new Matrix(4, 4);
        identity.setIdentityMatrix();

        /* foo should be invertible */
        bar = foo.inverse();
        assert bar != null;


        /* The process should leave foo as an identity */
        assert(foo.equalMatrix(identity, 0.0001));

        /* bar should be foo's inverse in either direction of multiplication */
        assert fooCopy != null;
        foo = fooCopy.multipliedBy(bar);
        assert(foo.equalMatrix(identity, 0.0001));
        foo = bar.multipliedBy(fooCopy);
        assert(foo.equalMatrix(identity, 0.0001));

    }

    @Test
    public void testConstructorArray() {
        assertThat(m42.getCols(), is(equalTo(2)));
        assertThat(m42.getRows(), is(equalTo(4)));
        assertThat(m23.getCols(), is(equalTo(3)));
        assertThat(m23.getRows(), is(equalTo(2)));
        assertThat(m24.getCols(), is(equalTo(4)));
        assertThat(m24.getRows(), is(equalTo(2)));
    }


    @Test
    public void testMultiplyTranspose() throws Exception {
        assertThat(m42.multiplyByTranspose(m32).getData(1, 1), is(equalTo(-28835.0)));
    }

    @Test
    public void testAddMatrices() throws Exception {
        assertThat(m42.addMatrix(m42).getData(1,1), is( equalTo(14.0) ));

    }
}