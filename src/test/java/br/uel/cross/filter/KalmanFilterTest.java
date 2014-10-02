package br.uel.cross.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class KalmanFilterTest {

    private KalmanFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new KalmanFilter(2, 1);

        /* The train state is a 2d vector containing position and velocity.
         Velocity is measured in position units per timestep units. */
        filter.setStateTransition(1.0, 1.0, 0.0, 1.0);

         /* We only observe position */
        filter.setObservationModel(1.0, 0.0);

        /* The covariance matrices are blind guesses */
        filter.getProcessNoiseCovariance().setIdentityMatrix();
        filter.getObservationNoiseCovariance().setIdentityMatrix();

        double deviation = 1000.0;
        filter.setStateEstimate(10.0*deviation, 1000.00);
        filter.getEstimateCovariance().setIdentityMatrix().scaleMatrix(deviation*deviation);
    }

    @After
    public void tearDown() throws Exception {
        filter = null;
    }

    @Test
    public void testTrain() {

        for (int i = 0; i < 10; ++i) {
            filter.setObservation((double) i);
            filter.update();
        }

//        Debug
//        System.out.println("Estimated position: " + filter.getStateEstimate().getData(0,0));
//        System.out.println("Estimated velocity: " + filter.getStateEstimate().getData(1,0));

        assertThat(filter.getStateEstimate().getData(0, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().getData(1, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().getData(0, 0) - 0.0005, is( lessThan(9.0) ));
        assertThat(filter.getStateEstimate().getData(1,0) - 0.0005, is( lessThan(1.0) ));
    }
}