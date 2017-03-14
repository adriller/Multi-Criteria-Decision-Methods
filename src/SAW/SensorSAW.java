package SAW;

import commom.Attribute;
import java.util.List;

public class SensorSAW {

    public SensorSAW(int id, List<Attribute> attr) {
        this.id = id;
        this.attributes = attr;
        this.sawValue = 0.0;
        this.rank = 0;
    }
    private int id;
    List<Attribute> attributes;
    private Double sawValue;
    private int rank;

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

    public Double getSawValue() {
        return sawValue;
    }

    public void setSawValue(Double sawValue) {
        this.sawValue = sawValue;
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
