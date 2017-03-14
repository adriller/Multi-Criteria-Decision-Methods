package VIKOR;

import commom.Attribute;
import java.util.List;

public class SensorVikor implements Cloneable{

    private int rank;

    private int id;

    public double getS() {
        return S;
    }

    public void setS(double s) {
        S = s;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }
    private double S;
    private double R;
    private double Q;
    List<Attribute> attributes;

    public SensorVikor(int id, List<Attribute> attr) {
        this.id = id;
        this.attributes = attr;
        this.S = 0.0;
        this.R = 0.0;
        this.Q = 0.0;
        this.rank = Integer.MAX_VALUE;
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

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
    
}
