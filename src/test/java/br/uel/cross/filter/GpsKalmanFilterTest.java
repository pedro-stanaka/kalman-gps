package br.uel.cross.filter;

import br.uel.cross.geoutils.Position;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;

public class GpsKalmanFilterTest {


    @Test
    public void testBearingNorth() throws Exception {
        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);
        for (int i = 0; i < 100; i++) {
            filter.updateVelocity2d(i * 0.0001, 0.0, 1.0);
        }

        double bearing = filter.getBearing();


        assertThat(Math.abs(bearing-0.0), is( lessThan(0.01) ));

        double[] velocity = filter.getVelocity();
        assertThat(Math.abs(velocity[0] - 0.0001), is( lessThan(1.0e-4) ) );
        assertThat(Math.abs(velocity[0]), is( lessThan(1.0e-3) ) );
    }




    @Test
    public void testBearingEast() throws Exception {

        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);
        for (int i = 0; i < 100; i++) {
            filter.updateVelocity2d(0.0, i * 0.0001, 1.0);
        }

        double bearing = filter.getBearing();

        assertThat(Math.abs(bearing-90.0), is( lessThan(0.01) ));



//         At this rate, it takes 10,000 timesteps to travel one longitude
//         unit, and thus 3,600,000 timesteps to travel the circumference of
//         the earth. Let's say one timestep is a second, so it takes
//         3,600,000 seconds, which is 60,000 minutes, which is 1,000
//         hours. Since the earth is about 25000 miles around, this means we
//         are traveling at about 25 miles per hour.
        double mph = filter.getMph();

        System.out.println("EAST MPH: " + mph);
        assertThat(mph*10, is(greaterThan(24.0)));
    }



    @Test
    public void testBearingSouth() throws Exception {

        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);
        for (int i = 0; i < 100; i++) {
            filter.updateVelocity2d(i * -0.0001, 0.0, 1.0);
        }

        double bearing = filter.getBearing();
        assertThat(Math.abs(bearing - 180.0), is(lessThan(0.01)));

    }


    @Test
    public void testBearingWest() throws Exception {

        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);
        for (int i = 0; i < 100; i++) {
            filter.updateVelocity2d(0.0, i* -0.0001, 1.0);
        }

        double bearing = filter.getBearing();
        assertThat(Math.abs(bearing - 270.0), is(lessThan(0.01)));

    }

    @Test
    public void testVariableTimestep() throws Exception {

        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);

        int eastDist = 0;

        for (int i = 0; i < 20; i++) {
            eastDist += i;
            filter.updateVelocity2d(0.0, eastDist* 0.0001, i);
        }

        double[] velocity = filter.getVelocity();
        assertThat(Math.abs(velocity[0]), is( lessThan(0.00001) ));
        assertThat(Math.abs(velocity[1] - 0.0001), is( lessThan(1.0e-4) ));

    }


    @Test
    public void testCalculateMph() throws Exception {
        GpsKalmanFilter filter = new GpsKalmanFilter(1.0);
        double mph = filter.calculateMph(new Position(39.315842, -120.167107), -0.000031, 0.000003);

        assertThat(Math.abs(mph - 7.74), is( lessThan(0.01) ));
    }



}