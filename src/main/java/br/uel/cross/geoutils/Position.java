package br.uel.cross.geoutils;

import java.util.Date;

/**
 * Pojo for a Position in a Trajectory
 */

public class Position {

    double lat;
    double lng;
    Date time;
    double velocity;

    public Position(double lat, double lng, Date time, double velocity) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.velocity = velocity;
    }

    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Position() {
        this.lat = 0.0;
        this.lng = 0.0;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
