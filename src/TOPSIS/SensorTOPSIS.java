package TOPSIS;

import commom.Attribute;
import java.util.List;

public class SensorTOPSIS {

    private int id;
    List<Attribute> attributes;
    private double distanceBest;
    private double distanceWorst;
    private double closeness;

    public SensorTOPSIS(int id, List<Attribute> attr) {
        this.id = id;
        this.attributes = attr;
        this.distanceBest = 0.0;
        this.distanceWorst = 0.0;
        this.closeness = 0.0;

    }

    public double getCloseness() {
        return closeness;
    }

    public void setCloseness(double closeness) {
        this.closeness = closeness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public double getDistanceBest() {
        return distanceBest;
    }

    public void setDistanceBest(double distanceBest) {
        this.distanceBest = distanceBest;
    }

    public double getDistanceWorst() {
        return distanceWorst;
    }

    public void setDistanceWorst(double distanceWorst) {
        this.distanceWorst = distanceWorst;
    }
}
