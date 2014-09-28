package br.uel.cross.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        filter.getStateEstimate().setMatrix(10.0*deviation, 10.0*deviation);
        filter.getEstimateCovariance().setMatrix(deviation*deviation, deviation*deviation, deviation*deviation, deviation*deviation);


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

        System.out.println("Estimate state: ");
        filter.getStateEstimate().print();
        System.out.println("\n");
    }
}