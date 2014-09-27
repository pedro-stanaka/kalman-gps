package br.uel.cross.filter.utils;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MatrixTest {


    private Matrix foo;
    private Matrix bar;
    private Matrix identity;
    private Matrix copy;

    @Before
    public void setUp() throws Exception {
        foo = new Matrix(4, 4);
        foo.setMatrix(1.0, 2.0, 3.0, 4.0,
                4.0, 1.0, 7.0, 9.0,
                0.0, 0.0, -4.0, -4.0,
                2.3, 3.4, 3.1, 0.0);
        copy = foo.clone();
        bar = new Matrix(4,4);
        identity = new Matrix(4,4);
        identity.setIdentityMatrix();
    }


    @Test
    public void testClone() throws Exception {
        Matrix tmp = foo.clone();
        assertThat(tmp.getData()[1][1], is(equalTo( foo.getData()[1][1] )));
        assertThat(tmp.getData()[2][2], is(equalTo( foo.getData()[2][2] )));
    }

    @Test
    public void testInverse() throws Exception {

        assertThat(foo.getData(0,0), is(equalTo(-0.2796610169491526)));
    }
}