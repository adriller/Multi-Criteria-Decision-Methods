/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdm;

import DAO.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import commom.Experiment;
import java.util.concurrent.ConcurrentHashMap;
import metrics.euclidiandistance.EuclidianObject;

/**
 *
 * @author lhnunes
 */
public class Pareto {

    public static Experiment exp = new Experiment();
    

    public static void main(String[] args) {
        MongoDB db = new MongoDB();
        int numberOfSensors = 0;
        double nSelected = 0;
        int nCriterion = 0;
        int dbOrder = 1;
        int limit = 1;

        String algorithm = "";

        if (args.length > 0) {
            algorithm = args[0];
            numberOfSensors = Integer.parseInt(args[1]);
            nSelected = Double.parseDouble(args[2]);
            nCriterion = Integer.parseInt(args[3]);
            limit = args.length > 4 ? Integer.parseInt(args[4]) : 0;
            dbOrder = args.length > 5 ? Integer.parseInt(args[5]) : 1;
        } else {
            algorithm = "topsis";
            numberOfSensors = exp.getNumberOfSensors();
            nSelected = exp.getNumberOfSelected();
            nCriterion = exp.getAttrSize();
            dbOrder = 1;
            limit = 2;
        }

        ConcurrentHashMap<Integer, EuclidianObject> distances = new ConcurrentHashMap<Integer, EuclidianObject>();
        DBCursor paretoResult = db.selectPareto(nCriterion);
        
        System.out.println("Updating db...");
        long time = System.currentTimeMillis();
        
        int i =0;
        for(DBObject obj : paretoResult){
            long sid = (int) obj.get("sid");
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append("front", (int) obj.get("front"));
            db.updateExperimentFront(algorithm,nCriterion, sid, updateObj);
            System.out.println(i++);
        }
               
        System.out.println("Update all for " + nCriterion + " in " + (System.currentTimeMillis() - time));
        return;
    }
}
