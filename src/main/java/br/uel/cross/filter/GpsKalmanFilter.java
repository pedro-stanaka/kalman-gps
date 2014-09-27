package br.uel.cross.filter;


import br.uel.cross.filter.utils.Matrix;
import br.uel.cross.geoutils.Position;

public class GpsKalmanFilter {
    private KalmanFilter filter;
    private final double EARTH_RADIUS_MILES = 3963.1676;
    private final double MILE_KILOMETER_FACTOR = 1.609344;


    // TODO fix this constructor it is killing OOP :/
    // If it is not possible to move this initialization code to KalmanFilter itself
    // try using Guice and named Dependency to get this instance of KalmanFilter
    public GpsKalmanFilter(double noise, double timeStep) {

        filter = new KalmanFilter(4, 2);



        /* Assuming the axes are rectilinear does not work well at the
         poles, but it has the bonus that we don't need to convert between
         lat/long and more rectangular coordinates. The slight inaccuracy
         of our physics model is not too important.
       */
        double v2p = 0.001;
        filter.getStateTransition().setIdentityMatrix();
        setSecondsPerTimeStep(timeStep);

        /* We observe (x, y) in each time step */
        filter.getObservationModel().setMatrix(1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0);
        /* Noise in the world. */
        double pos = 0.000001;
        filter.getProcessNoiseCovariance().setMatrix(pos, 0.0, 0.0, 0.0,
                0.0, pos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0);

        filter.getObservationNoiseCovariance().setMatrix(pos * noise, 0.0,
                0.0, pos * noise);

        // The start position is totally unknown, so give a high variance
        filter.getStateEstimate().setMatrix(0.0, 0.0, 0.0, 0.0);
        filter.getEstimateCovariance().setIdentityMatrix();
        double trillion = 10e12;
        filter.getEstimateCovariance().scaleMatrix(trillion);

    }


    /* The position units are in thousandths of latitude and longitude.
       The velocity units are in thousandths of position units per second.

       So if there is one second per timestep, a velocity of 1 will change
       the lat or long by 1 after a million timesteps.

       Thus a typical position is hundreds of thousands of units.
       A typical velocity is maybe ten.
    */
    private void setSecondsPerTimeStep(double timeStep) {

        /* unit_scaler accounts for the relation between position and
         velocity units */
        double unitScaler = 0.001;
        filter.getStateTransition().getData()[0][2] = filter.getStateTransition().getData()[1][3] = unitScaler * timeStep;
    }


    public Position getPosition(){
        Position position = new Position();
        position.setLat(filter.getStateEstimate().getData()[0][0] / 1000.0);
        position.setLng(filter.getStateEstimate().getData()[1][0] / 1000.0);
        return position;
    }

    public double[] getVelocity(){
        double deltas[] = new double[2];
        deltas[0] = (filter.getStateEstimate().getData()[2][0] / 1000.0);
        deltas[1] = (filter.getStateEstimate().getData()[3][0] / 1000.0);
        return deltas;
    }

    public double getBearing() {
        double lat, lng, deltaLat, deltaLng, x, y;
        Position position = getPosition();
        double[] velocity = getVelocity();

        // Radians conversions
        position.setLat(Math.toRadians(position.getLat()));
        position.setLng(Math.toRadians(position.getLng()));
        deltaLat = Math.toRadians(velocity[0]);
        deltaLng = Math.toRadians(velocity[1]);

        double lat1 = position.getLat() - deltaLat;
        y = Math.sin(deltaLng) * Math.cos(position.getLat());
        x = Math.cos(lat1) * Math.sin(lat1) * Math.cos(position.getLat()) * Math.cos(deltaLng);

        double bearing = Math.atan2(y, x);


        bearing = Math.toDegrees(bearing);

        while(bearing >= 360.0) {
            bearing -= 360.0;
        }
        while (bearing < 0.0) {
            bearing += 360.0;
        }

        return bearing;
    }

    public double calculateMph(Position position, double deltaLat, double deltaLng) {

        /* First, let's calculate a unit-independent measurement - the radii
           of the earth traveled in each second. (Presumably this will be
           a very small number.)
        */

        deltaLat = Math.toRadians(deltaLat);
        deltaLng = Math.toRadians(deltaLng);
        double lat = Math.toRadians(position.getLat());
        double lng = Math.toRadians(position.getLng());

        // Haversine formula
        double lat1 = lat - deltaLat;
        double sinHalfDlat = Math.sin(deltaLat / 2.0);
        double sinHalfDlng = Math.sin(deltaLng / 2.0);
        double a = Math.pow(sinHalfDlat, 2.0) + Math.cos(lat1) * Math.cos(lat) + Math.pow(sinHalfDlng, 2.0);
        double radiansPerSecond = 2.0 * Math.atan2(1000.0 * Math.sqrt(a), 1000.0 * Math.sqrt(1.0 - a));


        double milesPerSecond = radiansPerSecond * EARTH_RADIUS_MILES;
        return milesPerSecond * 60.0 * 60.0; // Miles per hour
    }

    public double getMph() {
        double[] velocity = getVelocity();
        return calculateMph(getPosition(), velocity[0], velocity[1]);
    }

}
