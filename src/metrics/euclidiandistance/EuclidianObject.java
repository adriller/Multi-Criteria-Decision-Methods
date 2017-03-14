/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics.euclidiandistance;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 *
 * @author lhnunes
 */
public class EuclidianObject {

    private double minDistance = Double.MAX_VALUE;
    private double maxDistance = -Double.MIN_VALUE;
    private long minPoint = Long.MIN_VALUE;
    private long maxPoint = Long.MIN_VALUE;
    
    private int sid;
    private int expId;
    private String algorithm;
    private double nSelected;
    private int nCriterion;
    private int nAttr;

    private BasicDBObject obj;
    
    public EuclidianObject() {
        this.maxDistance = Double.MIN_VALUE;
        this.minDistance = Double.MAX_VALUE;
        this.minPoint = Long.MIN_VALUE;
        this.maxPoint = Long.MIN_VALUE;
    }

    /**
     * @return the minDistance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * @param minDistance the minDistance to set
     */
    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * @return the maxDistance
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * @param maxDistance the maxDistance to set
     */
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * @return the minPoint
     */
    public long getMinPoint() {
        return minPoint;
    }

    /**
     * @param minPoint the minPoint to set
     */
    public void setMinPoint(long minPoint) {
        this.minPoint = minPoint;
    }

    /**
     * @return the maxPoint
     */
    public long getMaxPoint() {
        return maxPoint;
    }

    /**
     * @param maxPoint the maxPoint to set
     */
    public void setMaxPoint(long maxPoint) {
        this.maxPoint = maxPoint;
    }

    /**
     * @return the expId
     */
    public int getExpId() {
        return expId;
    }

    /**
     * @param expId the expId to set
     */
    public void setExpId(int expId) {
        this.expId = expId;
    }

    /**
     * @return the algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm the algorithm to set
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * @return the nSelected
     */
    public double getnSelected() {
        return nSelected;
    }

    /**
     * @param nSelected the nSelected to set
     */
    public void setnSelected(double nSelected) {
        this.nSelected = nSelected;
    }

    /**
     * @return the nCriterion
     */
    public int getnCriterion() {
        return nCriterion;
    }

    /**
     * @param nCriterion the nCriterion to set
     */
    public void setnCriterion(int nCriterion) {
        this.nCriterion = nCriterion;
    }

    /**
     * @return the nAttr
     */
    public int getnAttr() {
        return nAttr;
    }

    /**
     * @param nAttr the nAttr to set
     */
    public void setnAttr(int nAttr) {
        this.nAttr = nAttr;
    }

    /**
     * @return the obj
     */
    public BasicDBObject getObj() {
        return obj;
    }

    /**
     * @param obj the obj to set
     */
    public void setObj(BasicDBObject obj) {
        this.obj = obj;
    }

    /**
     * @return the sid
     */
    public int getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(int sid) {
        this.sid = sid;
    }

}
