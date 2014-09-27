package br.uel.cross.filter.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MatrixTest {


    private Matrix foo;

    @Before
    public void setUp() throws Exception {
        foo = new Matrix(4, 4);
        foo.setMatrix(1.0, 2.0, 3.0, 4.0,
                4.0, 1.0, 7.0, 9.0,
                0.0, 0.0, -4.0, -4.0,
                2.3, 3.4, 3.1, 0.0);
    }


    @Test
    public void testClone() throws Exception {
        Matrix clone = foo.clone();
        assertThat(clone.getData()[1][1], is(equalTo( foo.getData()[1][1] )));
        assertThat(clone.getData()[2][2], is(equalTo( foo.getData()[2][2] )));
    }

    @Test
    public void testInverse() throws Exception {
        assertThat(foo.getData(0,0), is(equalTo(-0.2796610169491526)));
    }
}