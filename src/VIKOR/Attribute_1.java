package VIKOR;

public class Attribute_1 {

    public Attribute_1(String name, String obj, Double value) {
        this.name = name;
        this.value = value;
        this.objective = obj;
    }
    private String name;
    private Double value;
    private String objective;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }
}
