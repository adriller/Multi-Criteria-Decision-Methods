/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics.euclidiandistance;

import DAO.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import commom.Attribute;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static mcdm.MCDM.exp;

/**
 *
 * @author lhnunes
 */
public class ComputationThread implements Runnable {

    private EuclidianObject eo;
    private DBObject expObj;
    private DBCursor paretoResult;
    private MongoDB db;
    private ConcurrentHashMap<Integer,EuclidianObject> map;

    public ComputationThread(EuclidianObject eo, DBObject obj, DBCursor pr, MongoDB d, ConcurrentHashMap<Integer, EuclidianObject> m) {
        this.eo = eo;
        this.expObj = obj;
        this.paretoResult = pr;
        this.db = d;
        this.map = m;
    }

    @Override
    public void run() {
        List<Attribute> lObjAttr = exp.getAttributes((DBObject) expObj.get("criteria"), eo.getnCriterion());
        for (DBObject paretoObj : paretoResult) {
            List<Attribute> lParetoAttr = exp.getAttributes((DBObject) paretoObj.get("Criteria"), eo.getnCriterion());
            //Compute the Euclidian distance
            double distance = euclidianDistance(lObjAttr, lParetoAttr);
            //Checks if is the point with min or max distance of pareto front
            if (distance > eo.getMaxDistance()) {
                eo.setMaxDistance(distance);
                eo.setMaxPoint((int) paretoObj.get("sid"));
            }
            
            if (distance < eo.getMinDistance()) {
                eo.setMinDistance(distance);
                eo.setMinPoint((int) paretoObj.get("sid"));
            }
        }
        /**
         * Creates a new object that will update the experiment object and
         */
        /*  store the euclidian distance and sid of the closer and farthest point in */
 /*  the pareto front */

        BasicDBObject minObj = new BasicDBObject();
        minObj.put("sid", eo.getMinPoint());
        minObj.put("distance", eo.getMinDistance());

        BasicDBObject maxObj = new BasicDBObject();
        maxObj.put("sid", eo.getMaxPoint());
        maxObj.put("distance", eo.getMaxDistance());

        BasicDBObject obj = new BasicDBObject();
        obj.put("minimumDistance", minObj);
        obj.put("maximumDistance", maxObj);

        eo.setSid((int) expObj.get("sid"));

        eo.setObj(obj);
        
        //Adicionar o hashmap aqui
        map.put(eo.getSid(), eo);
        //db.updateExperiment(eo.getExpId(), eo.getAlgorithm(), eo.getnSelected(), eo.getnCriterion(), eo.getSid(), eo.getObj());
    }

    /**
     * Computes the Euclidian Distance of two multidimensional points
     *
     * @param a
     * @param b
     * @return
     */
    public static double euclidianDistance(List<Attribute> a, List<Attribute> b) {
        int size = a.size() == b.size() ? a.size() : 0;
        double distance = 0.0;

        for (int i = 0; i < size; i++) {
            distance += Math.pow((a.get(i).getValue() - b.get(i).getValue()), 2);
        }
        return Math.sqrt(distance);
    }
}
