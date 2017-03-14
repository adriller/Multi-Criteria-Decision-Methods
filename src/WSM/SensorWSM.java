/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSM;
import commom.Attribute;
import java.util.List;
/**
 *
 * @author adriller
 */
public class SensorWSM {
    public SensorWSM(int id, List<Attribute> attr) {
        this.id = id;
        this.attributes = attr;
        this.WSMValue = 0.0;
    }
    private int id;
    List<Attribute> attributes;
    private Double WSMValue;
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
    public Double getWSMValue() {
        return WSMValue;
    }
    public void setWSMValue(Double WSMValue) {
        this.WSMValue = WSMValue;
    }
}