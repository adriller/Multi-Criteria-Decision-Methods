/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELECTREII;
import java.util.List;
import commom.Attribute;
/**
 *
 * @author Adriller Ferreira
 */
public class SensorELECTREII {
    private int id;
    private List<Attribute> attr;
    private Double electreValue = 0.0;
    private int rank = 0;

    public SensorELECTREII(int id, List<Attribute> attributes) {
        this.id = id;
        this.attr = attributes;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the attr
     */
    public List<Attribute> getAttributes() {
        return attr;
    }
    /**
     * @param attr the attr to set
     */
    public void setAttr(List<Attribute> attr) {
        this.attr = attr;
    }
     public Double getElectreValue() {
        return electreValue;
    }

    public void setElectreValue(Double sawValue) {
        this.electreValue = sawValue;
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