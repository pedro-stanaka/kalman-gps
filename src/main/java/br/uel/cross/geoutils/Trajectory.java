package br.uel.cross.geoutils;

import javafx.geometry.Pos;

import java.util.Date;
import java.util.List;

/**
 * Created by pedro on 26/09/14.
 */
public class Trajectory {

    private List<Position> positions;


    public Trajectory(List<Position> positions) {
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Position at(int i) {
        return positions.get(i);
    }

    public Position at(Date time) {
        for (Position position : positions) {
            if(position.getTime().equals(time)) return position;
        }

        return null;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
