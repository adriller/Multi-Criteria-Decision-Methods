package NSGA;

import DAO.MongoDB;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import commom.Attribute;
import java.util.ArrayList;
import java.util.List;

public class SensorParetto {

    private int id;
    List<Attribute> attributes;
    private Double crowdingDistance;
    private int np;
//    private List<SensorParetto> dominatedSensors;
//    private BigList<SensorParetto> dominatedSensors;
    private int rank;
    private int front;
    private MongoDB db;

    public SensorParetto(int id, List<Attribute> attr, int rank) {
        this.id = id;
        this.attributes = attr;
        this.crowdingDistance = 0.0;
        this.np = 0;
//        this.dominatedSensors = null;
        this.rank = rank;
        this.db = new MongoDB();
    }
    
    public SensorParetto(int id, List<Attribute> attr) {
        this.id = id;
        this.attributes = attr;
        this.crowdingDistance = 0.0;
        this.np = 0;
//        this.dominatedSensors = null;
        this.front = 0;
        this.db = new MongoDB();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(Double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public int getNp() {
        return np;
    }

    public void setNp(int np) {
        this.np = np;
    }

//    public List<SensorParetto> getDominatedSensors() {
//        return dominatedSensors;
//    }
//
//    public void setDominatedSensors(BigList<SensorParetto> dominatedSensors) {
//        this.dominatedSensors = dominatedSensors;
//    }
//
//    public void addToDominatedSensors(SensorParetto s) {
//        if (this.dominatedSensors != null) {
//            this.dominatedSensors.add(s);
//        } else {
//            this.dominatedSensors = new BigList<SensorParetto>();
//            this.dominatedSensors.add(s);
//        }
//    }
    
//    public void setDominatedSensors(List<SensorParetto> dominatedSensors) {
//        this.dominatedSensors = dominatedSensors;
//    }
//
//    public void addToDominatedSensors(SensorParetto s) {
//        if (this.dominatedSensors != null) {
//            this.dominatedSensors.add(s);
//        } else {
//            this.dominatedSensors = new ArrayList<SensorParetto>();
//            this.dominatedSensors.add(s);
//        }
//    }

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

    public void increaseNp() {
        this.np++;
    }

    public void decreaseNp() {
        this.np--;
    }

    /**
     * @return the front
     */
    public int getFront() {
        return front;
    }

    /**
     * @param front the front to set
     */
    public void setFront(int front) {
        this.front = front;
    }
    
    
    public void storeDominatedSensors(List<Integer> dominatedList){
        db.storeTempDominatedSensors(this.id, dominatedList,attributes.size());    
    }
    
    public List<Integer> getDominatedSensors(){
        DBCursor queryResult = db.selectTempDominatedSensors(this.id, attributes.size());
        
        List<Integer> list = new ArrayList<Integer>();
        for (DBObject obj : queryResult) {
            int id = (int) obj.get("sid");
            BasicDBList dominated = (BasicDBList) obj.get("dominated");
            for(int i = 0; i < dominated.size(); i++){
                list.add((int) dominated.get(i));
            }
        }
        return list;
    }
}
